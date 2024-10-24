package com.loser.backend.club.util;

import com.loser.backend.club.http.ContextHeader;

/**
 * @author ~~ trading.s
 * @date 23:57 10/03/21
 */
public final class ContextHolder {

    private static final ThreadLocal<ContextHeader> HOLDER = ThreadLocal.withInitial(ContextHeader::new);

    static public void set(ContextHeader header) {
        HOLDER.set(header);
    }

    static public ContextHeader get() {
        return HOLDER.get();
    }

    static public void clear() {
        HOLDER.remove();
    }
}
