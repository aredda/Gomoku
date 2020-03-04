package models;

import java.io.Serializable;

public enum MessageType implements Serializable
{
	KICK, INVALID_ROOM, GAME_START, PLAYER_INFO, ROOM_INFO, PLAY_CHIP
}
