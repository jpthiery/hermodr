package com.github.jpthiery.hermodr.broadcaster.infra.icecast;

import java.nio.file.Path;

public class BindingLibShout {

    private final long shoutInstancePtr;

    public BindingLibShout(Path libraryPath) {
        System.load(libraryPath.toString());
        shout_init();
        shoutInstancePtr = shout_new();
    }

    public String libVersion() {
        return shout_version(0, 0, 0);
    }

    //  Mapped from https://github.com/xiph/Icecast-libshout/blob/master/include/shout/shout.h.in

    private native static void shout_init();

    private native static void shout_shutdown();

    private native static long shout_new();

    private native static void shout_free(long shoutInstancePtr);

    /* Returns a statically allocated string describing the last shout error
     * to occur.  Only valid until the next libshout call on this shout_t */
    //const char *shout_get_error(shout_t *self);
    private native static String shout_get_error(long shoutInstancePtr);

    /* Return the error code (e.g. SHOUTERR_SOCKET) for this shout instance */
    //int shout_get_errno(shout_t *self);
    private native static int shout_get_errno(long shoutInstancePtr);

    /* returns SHOUTERR_CONNECTED or SHOUTERR_UNCONNECTED */
    //int shout_get_connected(shout_t *self);
    private native static int shout_get_connected(long shoutInstancePtr);

    // int shout_set_host(shout_t *self, const char *host);
    private native static int shout_set_host(long shoutInstancePtr, String host);

    // int shout_set_port(shout_t *self, unsigned short port);
    private native static int shout_set_port(long shoutInstancePtr, int port);

    //int shout_set_agent(shout_t *self, const char *agent);
    private native static int shout_set_agent(long shoutInstancePtr, String agent);

    //int shout_set_user(shout_t *self, const char *username);
    private native static int shout_set_user(long shoutInstancePtr, String user);

    //int shout_set_password(shout_t *, const char *password);
    private native static int shout_set_password(long shoutInstancePtr, String password);

    //  int shout_set_mount(shout_t *self, const char *mount);
    private native static int shout_set_mount(long shoutInstancePtr, String mount);

    private native static void open(long shoutInstancePtr);

    private native static String shout_version(int major, int minor, int patch);

    private final int SHOUTERR_SUCCESS = 0; /* No error */
    private final int SHOUTERR_INSANE = -1; /* Nonsensical arguments e.g. self being NULL */
    private final int SHOUTERR_NOCONNECT = -2; /* Couldn't connect */
    private final int SHOUTERR_NOLOGIN = -3; /* Login failed */
    private final int SHOUTERR_SOCKET = -4; /* Socket error */
    private final int SHOUTERR_MALLOC = -5; /* Out of memory */
    private final int SHOUTERR_METADATA = -6;
    private final int SHOUTERR_CONNECTED = -7; /* Cannot set parameter while connected */
    private final int SHOUTERR_UNCONNECTED = -8; /* Not connected */
    private final int SHOUTERR_UNSUPPORTED = -9; /* This libshout doesn't support the requested option */
    private final int SHOUTERR_BUSY = -10; /* Socket is busy */
    private final int SHOUTERR_NOTLS = -11; /* TLS requested but not supported by peer */
    private final int SHOUTERR_TLSBADCERT = -12; /* TLS connection can not be established because of bad certificate */
    private final int SHOUTERR_RETRY = -13; /* Retry last operation. */

}
