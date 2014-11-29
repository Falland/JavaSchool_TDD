package com.db.javaschool.tdd;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: Yury
 */
public class FirstAttemptFailedState implements ILoginState {

    @Override
    public ILoginState login(IAccount account, String password) {
        if (account.passwordMatches(password)) {
            account.setLoggedIn();
            return new AwaitingLoginState();
        }
        return new LastAttemptForCorrectPasswordState();
    }
}
