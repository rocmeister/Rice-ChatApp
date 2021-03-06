package common;

import java.rmi.Remote;
import java.util.List;
import java.rmi.RemoteException;

/**
 * Defines the RMI Remote Server Object that allows connections
 * at the ChatApp level (outside of individual rooms).
 *
 */
public interface IRemoteConnection extends Remote {
	
	/**
	 * The name the IStaffImplRemoteConnection object will be bound to in the RMI Registry
	 */
	static final String BOUND_NAME = "RemoteConnection";
	
	/**
	 * Used to connect to another chat app. When you call this method on a foreign IStaffImplRemoteConnection stub
	 * you give the foreign chatapp associated with this stub your IStaffImplRemoteConnection stub (so that they 
	 * know how to communicate with you). 
	 * 
	 * @param remoteConnectionStub - stub of IStaffImplRemoteConnection from the connector
	 * @throws RemoteException if an error occurs with remote connections.
	 */
	public void connect(IRemoteConnection remoteConnectionStub) throws RemoteException;
	
	/**
	 * Gets a list of chatroom id's that the remote chat app is actively in. 
	 * 
	 * It's okay to store these IChatRooms after receiving them (in a drop list or otherwise).
	 * 
	 * Notice that these IChatRooms may become stale (their memberlists are not updated to 
	 * reflect changes that occur after they are received).
	 * 
	 * In order to reduce the probability of joining a chatroom with a stale member list, do not join
	 * a chatroom that has been stored for any period of time. If you want to join a chatroom,
	 * -select which chatroom you want to join
	 * -do getChatRooms() to get the most recent list of IChatRooms
	 * -immediately compare the chatroom you have selected with the chatrooms from getChatRooms() using IChatRoom.getUUID() 
	 *  to determine which chatroom you wanted to join
	 * -immediately add your IMember stub to everyone else's member lists by sending a message with IJoinData to everyone in 
	 *  the newly obtained chatroom by using IChatRoom.sendMsgToAll() 
	 * 
	 * There is a nonzero probability that if multiple people attempt to join a chatroom simultaneously, they
	 * may end up with nonmatching member lists. See https://canvas.rice.edu/courses/14610/pages/chatapp-design-staff-feedback-3
	 * section 8. This seems to be an issue for Final Project.
	 * 
	 * @return A list of serialized IChatRooms
	 * @throws RemoteException if an error occurs with remote connections. 
	 */
	public List<IChatRoom> getChatRooms() throws RemoteException;
	

	/**
	 * This is used to send an already connected peer an invite to their chatroom. The
	 * inviter provides a chatroom to the invitee 
	 * @param chatRoom A serialized IChatRoom to join.
	 * @throws RemoteException if an error occurs with remote connections. 
	 */
	public void receiveInvite(IChatRoom chatRoom) throws RemoteException;
	
	/**
	 * Get the name of this user.
	 * @return The name of this user, as defined locally.
	 * @throws RemoteException if an error occurs with remote connections.
	 */
	public String getName() throws RemoteException;
	
	/**
	 * Tells the remote system that holds this IStaffImplRemoteConnection object that the given IStaffImplRemoteConnection stub 
	 * is no longer valid and should be removed from their system.
	 * 
	 * The application quitting process must entail telling all known IStaffImplRemoteConnection stubs that one is quitting.
	 * 
	 * You must leave all chatrooms (send a message with ILeaveData) before using this method.
	 * 
	 * @param remoteConnectionStub stub to be removed
	 * @throws RemoteException if an error occurs with remote connections.
	 */
	public void quit(IRemoteConnection remoteConnectionStub) throws RemoteException;

}