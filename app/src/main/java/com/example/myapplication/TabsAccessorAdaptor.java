package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

public class TabsAccessorAdaptor extends FragmentPagerAdapter {

    public TabsAccessorAdaptor(FragmentManager fn)
    {
        super(fn);
    }

    //Set up access to fragements
    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            //Home fragment is for chats
            case 0:
                ChatsFragment chatsFragment = new ChatsFragment();
                        return chatsFragment;

            //Fragment is for groups
            case 1:
                GroupsFragment groupsFragment = new GroupsFragment();
                return groupsFragment;

            //Fragment is for contacts
            case 2:
                ContactsFragment contactsFragment = new ContactsFragment();
                return contactsFragment;

            //Fragment is for requests
            case 3:
                FriendRequestsFragment friendRequestsFragment = new FriendRequestsFragment();
                return friendRequestsFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return "Chats";

            case 1:
                return "Groups";

            case 2:
                return "Contacts";

            case 3:
                return "Requests";

            default:
                return null;
        }
    }
}
