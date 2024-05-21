"use server";

import { serverEnv } from "../env/server/env";

export type Location = {
  id: string;
  name: string;
  latitude: number;
  longitude: number;
};

type LocationResponse = {
  locationId: string;
  name: string;
  latitude: number;
  longitude: number;
};

export async function getUserLocations(
  userId: string,
  token: string
): Promise<Location[]> {
  const apiUrl = new URL(
    `/user/${userId}/locations`,
    serverEnv.BACKEND_API_URL
  );
  const response = await fetch(apiUrl, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  });
  if (response.ok) {
    const data: LocationResponse[] = await response.json();
    return data.map((location) => ({
      id: location.locationId,
      name: location.name,
      latitude: location.latitude,
      longitude: location.longitude,
    }));
  }
  console.error("Failed to get locations", response);
  throw new Error("Failed to get locations");
}

type CreateLocationRequest = {
  name: string;
  latitude: number;
  longitude: number;
};

type CreateLocationResponse = {
  locationId: string;
  name: string;
  latitude: number;
  longitude: number;
};

export async function createUserLocation(
  userId: string,
  token: string,
  name: string,
  latitude: number,
  longitude: number
) {
  const apiUrl = new URL(
    `/user/${userId}/locations`,
    serverEnv.BACKEND_API_URL
  );

  const requestData: CreateLocationRequest = {
    name,
    latitude,
    longitude,
  };

  const response = await fetch(apiUrl, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(requestData),
  });
  if (response.ok) {
    const data: CreateLocationResponse = await response.json();
    console.log("Created location", data);
    return {
      id: data.locationId,
      name: data.name,
      latitude: data.latitude,
      longitude: data.longitude,
    };
  }

  console.error("Failed to create location", response);
  throw new Error("Failed to create location");
}

export async function deleteUserLocation(
  userId: string,
  locationId: string,
  token: string
) {
  const apiUrl = new URL(
    `/user/${userId}/locations/${locationId}`,
    serverEnv.BACKEND_API_URL
  );
  try {
    const response = await fetch(apiUrl, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    });
    if (response.ok) {
      console.log("Deleted location", locationId);
      return;
    }

    throw new Error("Failed to delete location");
  } catch (err) {
    console.error("Failed to delete location", err);
    throw new Error("Failed to delete location");
  }
}
