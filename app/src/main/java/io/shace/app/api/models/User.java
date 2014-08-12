package io.shace.app.api.models;

import android.app.Activity;
import android.content.Intent;

import java.util.Date;

import io.shace.app.App;
import io.shace.app.SplashScreenActivity_;
import io.shace.app.api.Model;
import io.shace.app.api.Task;
import io.shace.app.api.listeners.UserListener;
import io.shace.app.api.tasks.userTasks.Add;

/**
 * Created by melvin on 7/15/14.
 */
public class User extends Model {
    private String email;
    private String password = null;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private Date inscriptionDate = null;
    private boolean isAdmin = false;

    public User(String email, String password, String firstName, String lastName, Date birthDate) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    // Getter and setter, the cool stuff are in the bottom.

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Date getInscriptionDate() {
        return inscriptionDate;
    }

    public void setInscriptionDate(Date inscriptionDate) {
        this.inscriptionDate = inscriptionDate;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }


    // Actions, the cool stuffs

    public static boolean isAuthenticated() {
        return Token.get() != null;
    }

    public static boolean isLogged() {
        return Token.get().getType().equals(Token.TYPE_USER);
    }

    public static boolean isNotLogged() {
        return Token.get().getType().equals(Token.TYPE_GUEST);
    }

    /**
     * Save or update a user
     *
     * @param listener
     */
    public void save(UserListener listener) {
        if (isNotLogged()) {
            Task task = new Add(listener);
            task.exec(this);
        } else {
            // update
        }
    }

    /**
     * Sign out the update a user
     */
    public static void signOut() {
        Token.remove();
        // Todo Move to an Utility class
        Activity activity = App.getCurrentActivity();
        Intent intent = new Intent(activity, SplashScreenActivity_.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    /**
     * Retrieve a user from is ID
     * @param id
     *
     *
     */
    public void findById(int id) {

    }

//    public class Task {
//        private static final String TAG = "User";
//        protected Context sContext = App.getContext();
//
//        /**
//         * Sign out the current user
//         *
//         * @param activity
//         */
//        public void signOut(Activity activity) {
//            SharedPreferences settings = activity.getSharedPreferences("settings", Context.MODE_APPEND);
//            settings.edit().clear().apply();
//
//            // TODO: Put in a tool
//            Fragment fragment = new SignInFragment_();
//            FragmentManager fm = activity.getFragmentManager();
//            FragmentTransaction transaction = fm.beginTransaction();
//            transaction.replace(R.id.container, fragment);
//            transaction.commit();
//        }
//
//        static public void connectAsGuest(final EmptyCallback callback) {
//            HashMap<String, String> postData = new HashMap<String, String>();
//            postData.put("password", "");
//            postData.put("auto_renew", "true");
//
//            new AsyncApiCall().post(Routes.ACCESS_TOKEN, postData,
//                    new ApiResponse(new int[]{}) {
//                        @Override
//                        public void onSuccess(JSONObject response) {
//                            try {
//                                SharedPreferences.Editor settings = sContext.getSharedPreferences("settings", Context.MODE_APPEND).edit();
//                                settings.putString("accessToken", response.getString("token"));
//                                settings.putLong("creation", response.getLong("creation"));
//                                settings.putLong("expiration", response.getLong("expiration"));
//                                settings.apply();
//                            } catch (JSONException e) {
//                                Log.e(TAG, e.getMessage());
//                                ToastTools.use().longToast(R.string.internal_error);
//                            }
//
//                            if (callback != null) {
//                                callback.onSuccess();
//                            }
//                        }
//                    }
//            );
//        }
//
//        static public String getAccessToken() {
//            SharedPreferences settings = sContext.getSharedPreferences("settings", Context.MODE_PRIVATE);
//            return settings.getString("accessToken", null);
//        }
//
//
//        /**
//         * refresh the access_token
//         * TODO: API 2.0
//         *
//         * @param callback callback to call or null
//         */
//        @Deprecated
//        static public void refreshToken(final StringCallback callback) {
//            HashMap<String, String> data = new HashMap<String, String>();
//            data.put("access_token", getAccessToken());
//
//            new AsyncApiCall().noToken().get(Routes.ACCESS_TOKEN, data,
//                    new ApiResponse(new int[]{401}) {
//                        @Override
//                        public void onSuccess(JSONObject response) {
//                            String token = "";
//
//                            SharedPreferences.Editor settings = sContext.getSharedPreferences("settings", Context.MODE_APPEND).edit();
//                            try {
//                                token = response.getString("token");
//                                settings.putString("accessToken", token);
//                                settings.apply();
//                            } catch (JSONException e) {
//                                Log.e(TAG, "token not found");
//                            }
//                            callback.onSuccess(token);
//                        }
//                    }
//            );
//        }
//
//        /**
//         * Check if the user is properly logged with a user account
//         * DOES NOT VALIDATE THE ACCESS_TOKEN
//         *
//         * @return True if the user is logged, false otherwise
//         */
//        static public boolean isLogged() {
//            SharedPreferences settings = sContext.getSharedPreferences("settings", Context.MODE_PRIVATE);
//            String token = settings.getString("accessToken", null);
//            int userId = settings.getInt("userId", -1);
//            long creation = settings.getLong("creation", 0);
//            long expiration = settings.getLong("expiration", 0);
//
//            return (token != null && userId > -1 && creation > 0 && expiration > 0);
//        }
//
//
//        /**
//         * Check if the user has been authenticated using whether a user or guest account
//         * DOES NOT VALIDATE THE ACCESS_TOKEN
//         *
//         * @return True if the user is authenticated, false otherwise
//         */
//        static public boolean isAuthenticated() {
//            SharedPreferences settings = sContext.getSharedPreferences("settings", Context.MODE_PRIVATE);
//            String token = settings.getString("accessToken", null);
//            int userId = settings.getInt("userId", -1);
//            long creation = settings.getLong("creation", 0);
//            long expiration = settings.getLong("expiration", 0);
//
//            return (token != null && creation > 0 && expiration > 0);
//        }
//
//
//        /**
//         * Check if the access_token is still valid
//         * TODO: API 2.0
//         *
//         * @return True if the token has expired, false otherwise
//         */
//        @Deprecated
//        static public boolean sessionHasExpired() {
//            SharedPreferences settings = sContext.getSharedPreferences("settings", Context.MODE_PRIVATE);
//            String token = settings.getString("accessToken", null);
//            int userId = settings.getInt("userId", -1);
//            long creation = settings.getLong("creation", 0);
//            long expiration = settings.getLong("expiration", 0);
//
//            if (token != null && creation > 0 && expiration > 0) {
//                return (expiration < System.currentTimeMillis() / 1000);
//            }
//            return true;
//        }
//    }
}
