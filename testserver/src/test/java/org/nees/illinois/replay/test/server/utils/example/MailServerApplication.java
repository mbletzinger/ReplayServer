/**
 * Copyright 2005-2011 Noelios Technologies.
 * 
 * The contents of this file are subject to the terms of one of the following
 * open source licenses: LGPL 3.0 or LGPL 2.1 or CDDL 1.0 or EPL 1.0 (the
 * "Licenses"). You can select the license that you prefer but you may not use
 * this file except in compliance with one of these Licenses.
 * 
 * You can obtain a copy of the LGPL 3.0 license at
 * http://www.opensource.org/licenses/lgpl-3.0.html
 * 
 * You can obtain a copy of the LGPL 2.1 license at
 * http://www.opensource.org/licenses/lgpl-2.1.php
 * 
 * You can obtain a copy of the CDDL 1.0 license at
 * http://www.opensource.org/licenses/cddl1.php
 * 
 * You can obtain a copy of the EPL 1.0 license at
 * http://www.opensource.org/licenses/eclipse-1.0.php
 * 
 * See the Licenses for the specific language governing permissions and
 * limitations under the Licenses.
 * 
 * Alternatively, you can obtain a royalty free commercial license with less
 * limitations, transferable or non-transferable, directly at
 * http://www.noelios.com/products/restlet-engine
 * 
 * Restlet is a registered trademark of Noelios Technologies.
 */

package org.nees.illinois.replay.test.server.utils.example;

import java.util.HashMap;
import java.util.Map;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

/**
 * The reusable mail server application.
 */
public class MailServerApplication extends Application {

    /** Static list of accounts stored in memory. */
    private final Map<String, Account> accounts = new HashMap<String, Account>();

    /**
     * Constructor.
     */
    public MailServerApplication() {
        setName("RESTful Mail API application");
        setDescription("Example API for 'Restlet in Action' book");
        setOwner("Noelios Technologies");
        setAuthor("The Restlet Team");

        Account homer = new Account();
        homer.setFirstName("Homer");
        homer.setLastName("Simpson");
        homer.setLogin("chunkylover53");
        homer.setNickName("Personal mailbox of Homer");
        homer.setSenderName("Homer");
        homer.setEmailAddress("homer@simpson.org");
        homer.getContacts().add(new Contact("/accounts/bretzels34/"));
        homer.getContacts().add(new Contact("/accounts/jojo10/"));
        homer.getContacts().add(new Contact("/accounts/lisa1984/"));
        getAccounts().put("chunkylover53", homer);

        Account marge = new Account();
        marge.setFirstName("Marjorie");
        marge.setLastName("Simpson");
        marge.setLogin("bretzels34");
        marge.setNickName("Personal mailbox of Marge");
        marge.setSenderName("Marge");
        marge.setEmailAddress("homer@simpson.org");
        marge.getContacts().add(new Contact("/accounts/chunkylover53/"));
        marge.getContacts().add(new Contact("/accounts/jojo10/"));
        marge.getContacts().add(new Contact("/accounts/lisa1984/"));
        getAccounts().put("bretzels34", marge);

        Account bart = new Account();
        bart.setFirstName("Bartholomew");
        bart.setLastName("Simpson");
        bart.setLogin("jojo10");
        bart.setNickName("Personal mailbox of Bart");
        bart.setSenderName("Bart");
        bart.setEmailAddress("bart@simpson.org");
        bart.getContacts().add(new Contact("/accounts/chunkylover53/"));
        bart.getContacts().add(new Contact("/accounts/bretzels34/"));
        bart.getContacts().add(new Contact("/accounts/lisa1984/"));
        getAccounts().put("jojo10", bart);

        Account lisa = new Account();
        lisa.setFirstName("Lisa");
        lisa.setLastName("Simpson");
        lisa.setLogin("lisa1984");
        lisa.setNickName("Personal mailbox of Lisa");
        lisa.setSenderName("Lisa");
        lisa.setEmailAddress("lisa@simpson.org");
        lisa.getContacts().add(new Contact("/accounts/chunkylover53/"));
        lisa.getContacts().add(new Contact("/accounts/bretzels34/"));
        lisa.getContacts().add(new Contact("/accounts/jojo10/"));
        getAccounts().put("lisa1984", lisa);
    }

    /**
     * Creates a root Router to dispatch call to server resources.
     */
    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());
        router.attach("/", RootServerResource.class);
        router.attach("/accounts/", AccountServerResource.class);
        router.attach("/accounts/{accountId}/", AccountServerResource.class);
        return router;
    }

    /**
     * Returns the list of accounts stored in memory.
     * 
     * @return The list of accounts stored in memory.
     */
    public Map<String, Account> getAccounts() {
        return accounts;
    }

}
