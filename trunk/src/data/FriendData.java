package data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;

import log.Log;

import utils.ArrayList;
import zincfish.zincparser.xmlparser.ParserException;
import zincfish.zincparser.zmlparser.ZMLParser;

/**
 * <code>FriendData</code>封装用户的好友信息
 * 
 * @author Jarod Yv
 */
public class FriendData {
	private String id;
	private String time;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public ArrayList groupdatalist = new ArrayList();
	public ArrayList frienddatalist = new ArrayList();
	private static FriendData instance;

	private FriendData() {
	}

	public static FriendData getInstance() {
		if (instance == null) {
			instance = new FriendData();
		}
		return instance;
	}

	public static void setInstance(FriendData friendData) {
		instance = friendData;
	}

	public ArrayList getGroupdatalist() {
		return groupdatalist;
	}

	public void setGroupdatalist(ArrayList groupdatalist) {
		this.groupdatalist = groupdatalist;
	}

	public ArrayList getFriendByGroup(String groupID) {
		ArrayList temp = new ArrayList();
		for (int i = 0; i < frienddatalist.size(); i++) {
			Friend f = (Friend) frienddatalist.get(i);
			if (f.getGroup() != null && f.getGroup().indexOf(groupID) >= 0) {
				// log.debug("f==================="+f+"==="+i);
				temp.add(f);
			}
		}
		return temp;
	}

	public void setFrienddatalist(ArrayList frienddatalist) {
		this.frienddatalist = frienddatalist;
	}

	/**
	 * 注意!解析之后会改变<code>FriendData</code>单例,可以用<code>deepCopy()</code>
	 * 方法深拷贝一份FriendData对象作为备份
	 * 
	 * @param data
	 * @throws ParserException
	 * @throws IOException
	 */
	public void parseXMLData(byte[] data) throws ParserException, IOException {
		instance = new FriendData();

		ZMLParser parser = ZMLParser.getSNSParser();

		InputStream is = new ByteArrayInputStream(data);
		parser.setInput(is, "utf-8");
		parser.parse();
	}

	/**
	 * 拷贝单例对象以及所有的属性(深拷贝)
	 * 
	 * @return
	 */
	public FriendData deepCopy() {
		FriendData tempFriendData = new FriendData();
		tempFriendData.setId(id);
		tempFriendData.setTime(time);

		ArrayList gList = new ArrayList();
		for (int i = 0; i < groupdatalist.size(); i++) {
			gList.add(groupdatalist.get(i));
		}

		ArrayList fList = new ArrayList();
		for (int i = 0; i < frienddatalist.size(); i++) {
			fList.add(frienddatalist.get(i));
		}

		tempFriendData.setGroupdatalist(gList);
		tempFriendData.setFrienddatalist(fList);

		return tempFriendData;

	}

	public static final String RMS_NAME = "FRIENDSLIST";

	public void saveToRMS() throws RecordStoreFullException,
			RecordStoreNotFoundException, RecordStoreException, IOException {
		RecordStore rs = RecordStore.openRecordStore(RMS_NAME, true);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		DataOutputStream dos = new DataOutputStream(baos);
		// dos.writeShort(id!=null?id.length():0);
		dos.writeUTF(id != null ? id : "");
		// log.debug("id=="+id);
		// dos.writeShort(time!=null?time.length():0);
		dos.writeUTF(time != null ? time : "");
		// log.debug("time=="+time);
		dos.writeShort(frienddatalist.size());
		log.debug("frienddatalist.size()==" + frienddatalist.size());
		for (int i = 0; i < frienddatalist.size(); i++) {
			Friend f = (Friend) frienddatalist.get(i);
			// dos.writeShort(f.getMid()!=null?f.getMid().length():0);
			dos.writeUTF(f.getMid() != null ? f.getMid() : "");
			// log.debug("f.getMid()=="+f.getMid());
			// dos.writeShort(f.getName()!=null?f.getName().length():0);
			dos.writeUTF(f.getName() != null ? f.getName() : "");
			// log.debug("f.getName()=="+f.getName());
			// dos.writeShort(f.getGroup()!=null?f.getGroup().length():0);
			dos.writeUTF(f.getGroup() != null ? f.getGroup() : "");
			// log.debug("f.getGroup()=="+f.getGroup());
			// dos.writeShort(f.getDescript()!=null?f.getDescript().length():0);
			dos.writeUTF(f.getDescript() != null ? f.getDescript() : "");
			// log.debug("f.getDescript()=="+f.getDescript());
		}

		dos.writeShort(groupdatalist.size());
		for (int i = 0; i < groupdatalist.size(); i++) {
			Group g = (Group) groupdatalist.get(i);
			// dos.writeShort(g.getId()!=null?g.getId().length():0);
			dos.writeUTF(g.getId() != null ? g.getId() : "");
			// log.debug("g.getId()=="+g.getId());

			// dos.writeShort(g.getName()!=null?g.getName().length():0);
			dos.writeUTF(g.getName() != null ? g.getName() : "");
			// log.debug("g.getName()=="+g.getName());
			// dos.writeShort(g.getDescript()!=null?g.getDescript().length():0);
			dos.writeUTF(g.getDescript() != null ? g.getDescript() : "");
			// log.debug("g.getDescript()=="+g.getDescript());
		}

		baos.flush();
		dos.flush();

		byte[] frData = baos.toByteArray();

		if (rs.getNumRecords() <= 0) {
			rs.addRecord(frData, 0, frData.length);
		} else {
			rs.setRecord(1, frData, 0, frData.length);
		}

		rs.closeRecordStore();

		baos.close();
		dos.close();
	}

	private Log log = Log.getLog(this.getClass().getName());

	public FriendData readFromRMS() throws RecordStoreFullException,
			RecordStoreNotFoundException, RecordStoreException, IOException {
		RecordStore rs = RecordStore.openRecordStore(RMS_NAME, true);
		if (rs != null && rs.getNumRecords() > 0) {
			byte[] frData = rs.getRecord(1);
			ByteArrayInputStream bais = new ByteArrayInputStream(frData);
			DataInputStream dis = new DataInputStream(bais);

			FriendData friendData = new FriendData();

			String id = dis.readUTF();
			friendData.setId(id);
			// log.debug("id=="+id);

			String time = dis.readUTF();
			friendData.setTime(time);
			// log.debug("time=="+time);

			short fListSize = dis.readShort();
			for (int i = 0; i < fListSize; i++) {
				Friend frd = new Friend();

				String mid = dis.readUTF();
				frd.setMid(mid);
				// log.debug("mid=="+mid);

				String name = dis.readUTF();
				frd.setName(name);
				// log.debug("name=="+name);

				String group = dis.readUTF();
				frd.setGroup(group);
				// log.debug("group=="+group);

				String desc = dis.readUTF();
				frd.setDescript(desc);
				// log.debug("description=="+desc);

				friendData.frienddatalist.add(frd);

			}

			short gListSize = dis.readShort();
			for (int i = 0; i < gListSize; i++) {
				Group grp = new Group();

				String gid = dis.readUTF();
				grp.setId(gid);
				// log.debug("gid=="+gid);

				String name = dis.readUTF();
				grp.setName(name);
				// log.debug("name=="+name);

				String desc = dis.readUTF();
				grp.setDescript(desc);
				// log.debug("description=="+desc);

				friendData.groupdatalist.add(grp);

			}

			bais.close();
			dis.close();

			rs.closeRecordStore();

			return friendData;
		}
		return null;

		// TODO
	}

}
