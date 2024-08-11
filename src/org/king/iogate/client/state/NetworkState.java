package org.king.iogate.client.state;

import org.king.iogate.client.state.type.ClientType;
import org.king.iogate.client.state.type.LoginType;
import org.king.iogate.client.state.type.MatchType;

public class NetworkState {

    public static ClientType clientState = ClientType.NOT_CONNECTED;

    public static LoginType loginState = LoginType.NOT_LOGGED_IN;

    public static MatchType matchState = MatchType.NOT_MATCHED;

}
