package com.stoptheballs.app;


import com.apps.mobile.android.commons.achievements.IAchievementsManager;
import com.apps.mobile.android.commons.ads.api.IAdsConfigurations;
import com.apps.mobile.android.commons.app.Application_Base;
import com.apps.mobile.android.commons.cfg.app.IAppConfig;
import com.apps.mobile.android.commons.cfg.appstore.IAppStore;
import com.apps.mobile.android.commons.cfg.colours.ConfigurationUtils_Colours;
import com.apps.mobile.android.commons.cfg.menu.ConfigurationUtils_Base_MenuMain;
import com.apps.mobile.android.commons.engagement.ILeaderboardsProvider;
import com.apps.mobile.android.commons.engagement.leaderboards.LeaderboardsProvider_Base;
import com.apps.mobile.android.commons.events.api.IEventsManager;
import com.apps.mobile.android.commons.graphics2d.app.Application_2D_Base;
import com.apps.mobile.android.commons.graphics2d.model.IWorld;
import com.apps.mobile.android.commons.model.GameData_Base;
import com.apps.mobile.android.commons.model.UserSettings_Base;
import com.apps.mobile.android.commons.ui.utils.DebugUtils;
import com.stoptheballs.lib.BuildConfig;
import com.stoptheballs.achievements.AchievementsManager_StopTheBalls;
import com.stoptheballs.cfg.app.AppConfig_StopTheBalls;
import com.stoptheballs.cfg.world.ConfigurationUtils_Level;
import com.stoptheballs.events.EventsManager_StopTheBalls;
import com.stoptheballs.main.Activity_Result;
import com.stoptheballs.model.GameData_StopTheBalls;
import com.stoptheballs.model.UserSettings_StopTheBalls;
import com.stoptheballs.model.WorldGenerator_StopTheBalls;


public abstract class Application_StopTheBalls extends Application_2D_Base {
	
	
	protected IAppConfig appConfig 					= new AppConfig_StopTheBalls();
	
	
	@Override
	public void onCreate() {
		
		super.onCreate();
		//Called when the application is starting, before any other application objects have been created.
		
		System.out.println("Application_EC: onCreate called " + System.currentTimeMillis());
		
		ConfigurationUtils_Colours.class.getName();
		
		ConfigurationUtils_Level.createInstance();
		
		ConfigurationUtils_Base_MenuMain.createInstance();
	}
	
	
	@Override
	public IAppConfig getAppConfig() {
		return appConfig;
	}
	
	
	@Override
	protected IAchievementsManager createAchievementsManager() {
		return new AchievementsManager_StopTheBalls(this);
	}
	
	
	@Override
	protected IEventsManager createEventsManager() {
		return new EventsManager_StopTheBalls(getExecutor(), getAnalytics(), getAchievementsManager());
	}
	
	
	@Override
	protected ILeaderboardsProvider createLeaderboardsProvider() {
		return new LeaderboardsProvider_Base(this, Activity_Result.class);
	}
	
	
	@Override
	public void setNextLevel() {
		getUserSettings().modeID = ConfigurationUtils_Level.getInstance().getNextConfigID(getUserSettings().modeID);
		Application_Base.getInstance().storeUserSettings();
		System.out.println("Next level: " + getUserSettings().modeID);
	}
	
	
	@Override
	public IWorld createNewWorld() {
		return WorldGenerator_StopTheBalls.generate(this, ConfigurationUtils_Level.getInstance().getConfigByID(Application_Base.getInstance().getUserSettings().modeID));
	}
	
	
	@Override
	public GameData_Base createGameDataObject() {
		
		System.out.println("GAMEDATA CREATE");
		
		GameData_StopTheBalls result = new GameData_StopTheBalls();
		
		int levelID = getUserSettings().modeID;
		result.world = WorldGenerator_StopTheBalls.generate(this, ConfigurationUtils_Level.getInstance().getConfigByID(levelID));
		
		result.timestamp_lastborn = System.currentTimeMillis();
		
		return result;
	}
	
	
	@Override
	protected UserSettings_Base createUserSettingsObject() {
		return new UserSettings_StopTheBalls();
	}
	
	
	@Override
	public boolean isTestMode() {
		boolean productiveMode = !BuildConfig.DEBUG || !DebugUtils.isDebuggable(this);
		return !productiveMode;
	}
}
