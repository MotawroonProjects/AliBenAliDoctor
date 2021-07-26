package com.alibenalidoctor.interfaces;


public interface Listeners {



    interface SettingAction{
        void onLogIn();
        void onFavorite();
        void onEditProfile();
        void onLanguageSetting();
        void onAbout();
        void onTerms();
        void onContactUs();
        void onLogout();
    }
    interface HomeListener{

        void status();
        void profile();
        void patient();

        void setting();
        void notification();
        void langChange();
    }

    interface BackListener
    {
        void back();
    }


}
