package com.example.connect4.dto;

import java.io.Serializable;

import com.example.connect4.enums.DiskColor;

import lombok.Data;

@Data
public class Player implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = -7134159378434746510L;

	private String userId;

	private DiskColor diskColor;

}
