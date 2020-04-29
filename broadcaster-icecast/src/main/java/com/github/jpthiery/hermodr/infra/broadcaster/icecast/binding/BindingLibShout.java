package com.github.jpthiery.hermodr.broadcaster.infra.broadcaster.icecast.binding;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public class BindingLibShout {

    private static boolean loaded = false;

    private final long shoutInstancePtr;

    public BindingLibShout(Path libraryPath) {
        synchronized (this) {
            if (!loaded) {
                System.load(libraryPath.toString());
                loaded = true;
            }
        }
        shout_init();
        shoutInstancePtr = shout_new();
    }

    public String libVersion() {
        return shout_version(0, 0, 0);
    }

    public BindingResult open() {
        return execute(() -> shout_open(shoutInstancePtr));
    }

    public BindingResult close() {
        return execute(() -> shout_close(shoutInstancePtr))
                .then(bindingLibShout -> {
                    shout_free(shoutInstancePtr);
                    return BindingResult.success(bindingLibShout);
                });
    }

    public BindingResult setHost(String host) {
        return execute(() -> shout_set_host(shoutInstancePtr, host));
    }

    public BindingResult setProtocol(int protocol) {
        return execute(() -> shout_set_protocol(shoutInstancePtr, protocol));
    }

    public BindingResult useHttp() {
        return setProtocol(SHOUT_PROTOCOL_HTTP);
    }

    public BindingResult useIcy() {
        return setProtocol(SHOUT_PROTOCOL_ICY);
    }

    public BindingResult useRoarAudio() {
        return setProtocol(SHOUT_PROTOCOL_ROARAUDIO);
    }

    public BindingResult setPort(int port) {
        return execute(() -> shout_set_port(shoutInstancePtr, port));
    }

    public BindingResult setUser(String user) {
        return execute(() -> shout_set_user(shoutInstancePtr, user));
    }

    public BindingResult setPassword(String password) {
        return execute(() -> shout_set_password(shoutInstancePtr, password));
    }

    public BindingResult setMount(String mount) {
        return execute(() -> shout_set_mount(shoutInstancePtr, mount));
    }

    public BindingResult setFormat(int format) {
        return execute(() -> shout_set_format(shoutInstancePtr, format));
    }

    public BindingResult sendMp3() {
        return setFormat(SHOUT_FORMAT_MP3);
    }

    public BindingResult sendOgg() {
        return setFormat(SHOUT_FORMAT_OGG);
    }

    public BindingResult send(byte[] data, int length) {
        return execute(() -> {
            var res = shout_send(shoutInstancePtr, data, length);
            shout_sync(shoutInstancePtr);
            return res;
        });

    }

    private interface Callback {
        int exec();
    }

    private @NotNull BindingResult execute(Callback callback) {
        var res = callback.exec();
        if (res == SHOUTERR_SUCCESS) {
            return BindingResult.success(this);
        } else {
            return BindingResult.failed(this, shout_get_error(shoutInstancePtr));
        }
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

    // int shout_set_mount(shout_t *self, const char *mount);
    private native static int shout_set_mount(long shoutInstancePtr, String mount);

    // int shout_set_public(shout_t *self, unsigned int make_public);
    private native static int shout_set_public(long shoutInstancePtr, int makePublic);

    // int shout_set_format(shout_t *self, unsigned int format); // obsolete
    private native static int shout_set_format(long shoutInstancePtr, int format);

    //int shout_set_protocol(shout_t *self, unsigned int protocol);
    private native static int shout_set_protocol(long shoutInstancePtr, int protocol);

    private native static int shout_open(long shoutInstancePtr);

    private native static int shout_close(long shoutInstancePtr);

    /* Send data to the server, parsing it for format specific timing info */
    // int shout_send(shout_t *self, const unsigned char *data, size_t len);
    private native static int shout_send(long shoutInstancePtr, byte[] data, int setDataLength);

    private native static void shout_sync(long shoutInstancePtr);

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

    public static final int SHOUT_PROTOCOL_HTTP = 0;
    public static final int SHOUT_PROTOCOL_ICY = 2;
    public static final int SHOUT_PROTOCOL_ROARAUDIO = 3;

    public static final int SHOUT_FORMAT_OGG = 0; /* Ogg */
    public static final int SHOUT_FORMAT_MP3 = 1; /* MP3 */
    public static final int SHOUT_FORMAT_WEBM = 2; /* WebM */
    public static final int SHOUT_FORMAT_WEBMAUDIO = 3; /* WebM, audio only, obsolete. Only used by shout_set_format() */
    public static final int SHOUT_FORMAT_MATROSKA = 4; /* Matroska */

    /* Possible TLS modes */
    private final int SHOUT_TLS_DISABLED = 0; /* Do not use TLS at all */
    private final int SHOUT_TLS_AUTO = 1; /* Autodetect which TLS mode to use if any */
    private final int SHOUT_TLS_AUTO_NO_PLAIN = 2; /* Like SHOUT_TLS_AUTO_NO_PLAIN but does not allow plain connections */
    private final int SHOUT_TLS_RFC2818 = 11; /* Use TLS for transport layer like HTTPS [RFC2818] does. */
    private final int SHOUT_TLS_RFC2817 = 12; /* Use TLS via HTTP Upgrade:-header [RFC2817]. */

    public static final String SHOUT_AI_BITRATE = "bitrate";
    public static final String SHOUT_AI_SAMPLERATE = "samplerate";
    public static final String SHOUT_AI_CHANNELS = "channels";
    public static final String SHOUT_AI_QUALITY = "quality";

}
