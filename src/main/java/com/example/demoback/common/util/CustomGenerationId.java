package com.example.demoback.common.util;

/***
 *
 * 描述：自定义id生成
 */

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

public class CustomGenerationId implements IdentifierGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        // TODO Auto-generated method stub
        return getId();
    }
    /**
     * 	该方法需要是线程安全的
     * @return
     */
    public String getId() {
        synchronized (CustomGenerationId.class) {
            return KeyWord.getKeyWordTime();
        }
    }
}
