package com.EzTexting;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

/**
  */
public class EzTextingConnection {

    private final String user;
    private final String password;
    private final String baseUrl;
    private final Encoding encoding;

    public EzTextingConnection(String user, String password, String baseUrl, Encoding encoding) {
        this.user = user;
        this.password = password;
        this.baseUrl = baseUrl;
        this.encoding = encoding;
    }

    public EzTextingConnection(String user, String password, String baseUrl) {
        this(user, password, baseUrl, Encoding.JSON);
    }

    public EzTextingConnection(String user, String password, Encoding encoding) {
        this(user, password, "https://app.eztexting.com", encoding);
    }

    /**
     * Constructor uses default url and JSON encoding.
     * @param user
     * @param password
     */
    public EzTextingConnection(String user, String password) {
        this(user, password, "https://app.eztexting.com");
    }

    /**
     * Create object in Ez Texting.
     * @param object
     * @return
     * @throws IOException
     * @throws EzTextingException
     * @throws Exception
     */
    public BaseObject create(BaseObject object) throws IOException, EzTextingException, Exception {
        String response = post(object.getBaseUrl(), object.getParams());
        return encoding.parseObjectResponse((Class<BaseObject>) object.getClass(), response);
    }

    /**
     * Update object in Ez Texting.
     * @param object
     * @return
     * @throws IOException
     * @throws EzTextingException
     * @throws Exception
     */
    public BaseObject update(BaseObject object) throws IOException, EzTextingException, Exception {
        String response = post(object.getUpdateUrl(), object.getParams());
        if (object.hasResponseAfterUpdate())
        {
            return encoding.parseObjectResponse((Class<BaseObject>) object.getClass(), response);
        }
        return null;
    }

    /**
     * Delete obkect from Ez Texting.
     * @param object
     * @throws IOException
     * @throws EzTextingException
     * @throws Exception
     */
    public void delete(BaseObject object) throws IOException, EzTextingException, Exception {
        post(addUrlParam(object.getUpdateUrl(), "_method=DELETE"), object.getParams());
    }

    /**
     * Get contact by ID.
     * @param id
     * @return
     * @throws IOException
     * @throws EzTextingException
     * @throws Exception
     */
    public Contact getContact(String id) throws IOException, EzTextingException, Exception {
        return (Contact) get(new Contact(id));
    }

    /**
     * Get group by ID.
     * @param id
     * @return
     * @throws IOException
     * @throws EzTextingException
     * @throws Exception
     */
    public Group getGroup(String id) throws IOException, EzTextingException, Exception {
        return (Group) get(new Group(id));
    }

    /**
     * Get folder by ID.
     * @param id
     * @return
     * @throws IOException
     * @throws EzTextingException
     * @throws Exception
     */
    public Folder getFolder(String id) throws IOException, EzTextingException, Exception {
        return (Folder) get(new Folder(id));
    }

    /**
     * Get InboxMessage by ID.
     * @param id
     * @return
     * @throws IOException
     * @throws EzTextingException
     * @throws Exception
     */
    public InboxMessage getInboxMessage(String id) throws IOException, EzTextingException, Exception {
        return (InboxMessage) get(new InboxMessage(id));
    }

    public BaseObject get(BaseObject object) throws IOException, EzTextingException, Exception {
        String response = get(object.getUpdateUrl());
        return encoding.parseObjectResponse((Class<BaseObject>) object.getClass(), response);
    }

    /**
     * Get a list of contacts stored in your Ez Texting contact list.
     *
     * @param query (Optional) Search contacts by first name / last name / phone number
     * @param source (Optional) Source of contacts. Available values: 'Unknown', 'Manually Added', 'Upload', 'Web Widget', 'API', 'Keyword'
     * @param optout (Optional) Opted out / opted in contacts. Available values: true, false.
     * @param group (Optional) Name of the group the contacts belong to
     * @param sortBy (Optional) Property to sort by. Available values: PhoneNumber, FirstName, LastName, CreatedAt
     * @param sortDir (Optional) Direction of sorting. Available values: asc, desc
     * @param itemsPerPage (Optional) Number of results to retrieve. By default, 10 most recently added contacts are retrieved.
     * @param page (Optional) Page of results to retrieve
     * @return
     */
    public List<BaseObject> getContacts(String query, String source, String optout, String group, String sortBy, String sortDir,
                                        String itemsPerPage, String page) throws IOException, EzTextingException, Exception {
        Hashtable<String, Object> params = new Hashtable<String, Object>();
        BaseObject.putNotNull(params, "query", query);
        BaseObject.putNotNull(params, "source", source);
        BaseObject.putNotNull(params, "optout", optout);
        BaseObject.putNotNull(params, "group", group);
        BaseObject.putNotNull(params, "sortBy", sortBy);
        BaseObject.putNotNull(params, "sortDir", sortDir);
        BaseObject.putNotNull(params, "itemsPerPage",itemsPerPage );
        BaseObject.putNotNull(params, "page", page);
        BaseObject c = new Contact(null);
        String response = get(c.getBaseUrl(), params);
        return encoding.parseObjectsResponse((Class<BaseObject>) c.getClass(), response);
    }

    /**
     * Get a list of groups stored in your Ez Texting account.
     *
     * @param sortBy (Optional) Property to sort by. Available values: Name
     * @param sortDir (Optional) Direction of sorting. Available values: asc, desc
     * @param itemsPerPage (Optional) Number of results to retrieve. By default, first 10 groups sorted in alphabetical order are retrieved.
     * @param page (Optional) Page of results to retrieve
     * @return
     * @throws IOException
     * @throws EzTextingException
     * @throws Exception
     */
    public List<BaseObject> getGroups(String sortBy, String sortDir, String itemsPerPage, String page) throws IOException, EzTextingException, Exception {
        Hashtable<String, Object> params = new Hashtable<String, Object>();
        BaseObject.putNotNull(params, "sortBy", sortBy);
        BaseObject.putNotNull(params, "sortDir", sortDir);
        BaseObject.putNotNull(params, "itemsPerPage",itemsPerPage );
        BaseObject.putNotNull(params, "page", page);
        BaseObject g = new Group(null);
        String response = get(g.getBaseUrl(), params);
        return encoding.parseObjectsResponse((Class<BaseObject>) g.getClass(), response);
    }

    /**
     * Get all Folders in your Ez Texting Inbox.
     *
     * @return
     * @throws IOException
     * @throws EzTextingException
     * @throws Exception
     */
    public List<BaseObject> getFolders() throws IOException, EzTextingException, Exception {
        Hashtable<String, Object> params = new Hashtable<String, Object>();
        BaseObject g = new Folder(null);
        String response = get(g.getBaseUrl(), params);
        return encoding.parseObjectsResponse((Class<BaseObject>) g.getClass(), response);
    }

    /**
     * Get all incoming text messages in your Ez Texting Inbox.
     *
     * @param folderId (Optional) Get messages from the selected folder. If FolderID is not given then request will return messages in your Inbox and all folders.
     * @param search (Optional) Get messages which contain selected text or which are sent from selected phone number.
     * @param sortBy (Optional) Property to sort by. Available values: ReceivedOn, PhoneNumber, Message
     * @param sortDir (Optional) Direction of sorting. Available values: asc, desc
     * @param itemsPerPage (Optional) Number of results to retrieve. By default, first 10 groups sorted in alphabetical order are retrieved.
     * @param page (Optional) Page of results to retrieve
     * @return
     * @throws IOException
     * @throws EzTextingException
     * @throws Exception
     */
    public List<BaseObject> getInboxMessages(String folderId, String search, String sortBy, String sortDir, String itemsPerPage, String page) throws IOException, EzTextingException, Exception {
        Hashtable<String, Object> params = new Hashtable<String, Object>();
        BaseObject.putNotNull(params, "FolderID", folderId);
        BaseObject.putNotNull(params, "Search", search);
        BaseObject.putNotNull(params, "sortBy", sortBy);
        BaseObject.putNotNull(params, "sortDir", sortDir);
        BaseObject.putNotNull(params, "itemsPerPage",itemsPerPage );
        BaseObject.putNotNull(params, "page", page);
        BaseObject g = new InboxMessage(null);
        String response = get(g.getBaseUrl(), params);
        return encoding.parseObjectsResponse((Class<BaseObject>) g.getClass(), response);
    }

    public void moveMessageToFolder(String messageId, String folderId) throws IOException, EzTextingException, Exception {
        Hashtable<String, Object> params = new Hashtable<String, Object>();
        BaseObject.putNotNull(params, "ID", messageId);
        BaseObject.putNotNull(params, "FolderID", folderId);
        BaseObject g = new InboxMessage(null);
        post(g.getBaseUrl()+"/?_method=move-to-folder", params);
    }

    public void moveMessagesToFolder(List<String> messageIds, String folderId) throws IOException, EzTextingException, Exception {
        Hashtable<String, Object> params = new Hashtable<String, Object>();
        params.put("ID", messageIds);
        BaseObject.putNotNull(params, "FolderID", folderId);
        BaseObject g = new InboxMessage(null);
        post(g.getBaseUrl()+"/?_method=move-to-folder", params);
    }

    private String get(String relUrl) throws IOException, EzTextingException, Exception {
        return get(relUrl, new Hashtable<String, Object>());
    }

    private String get(String relUrl, Hashtable<String, Object> getParams) throws IOException, EzTextingException, Exception {
        String fullUrl = addUrlParam(baseUrl+relUrl, encoding.getEncodingParam());
        addLoginInfo(getParams);

        String params = encodeParams(getParams);
        fullUrl = addUrlParam(fullUrl, params);

        URL url = new URL(fullUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();



        int responseCode = conn.getResponseCode();

        boolean isSuccessResponse = responseCode < 400;

        String res = readRespone(conn, isSuccessResponse);
        if (!isSuccessResponse)
        {
            if (responseCode < 500)
                throw new EzTextingException(responseCode, encoding.parseErrors(res));
            else
                throw new EzTextingException(responseCode, res);
        }
        return res;
    }

    private void addLoginInfo(Hashtable<String, Object> requestParams) {
        requestParams.put("User", user);
        requestParams.put("Password", password);
    }

    private String post(String relUrl, Hashtable<String, Object> postParams) throws IOException, EzTextingException, Exception {
        String fullUrl = addUrlParam(baseUrl+relUrl, encoding.getEncodingParam());
        URL url = new URL(fullUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");

        addLoginInfo(postParams);

        String params = encodeParams(postParams);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(params);
        wr.flush();

        int responseCode = conn.getResponseCode();

        boolean isSuccessResponse = responseCode < 400;

        String res = readRespone(conn, isSuccessResponse);
        wr.close();
        if (!isSuccessResponse)
        {
            if (responseCode < 500)
                throw new EzTextingException(responseCode, encoding.parseErrors(res));
            else
                throw new EzTextingException(responseCode, res);
        }
        return res;
    }

    private String readRespone(HttpURLConnection conn, boolean successResponse) throws IOException {
        InputStream responseStream = successResponse ? conn.getInputStream() : conn.getErrorStream();
        String res="";
        if (responseStream != null) {
            res = IOUtils.toString(responseStream);
            responseStream.close();
        }
        return res;
    }

    private static String encodeParams(Hashtable<String, Object> params) throws UnsupportedEncodingException {
        String res = "";
        Enumeration<String> keys = params.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            Object val = params.get(key);
            if (val instanceof String)
            {
                String sval = (String) val;
                res += "&"+key+"="+ java.net.URLEncoder.encode(sval, "ISO-8859-1");
            }
            else
            {   // this should be List<String>, put multiple values to the same key
                List<String> lval = (List<String>) val;
                for (int i = 0; i < lval.size(); i++) {
                    String v = lval.get(i);
                    res += "&"+key+"="+ java.net.URLEncoder.encode(v, "ISO-8859-1");
                }
            }
        }
        if (res.length()>0) {
            res = res.substring(1);
        }
        return res;
    }

    private static String addUrlParam(String url, String param) {
        char divider = url.indexOf('?')== -1 ? '?' : '&';
        return url + divider + param;
    }

}