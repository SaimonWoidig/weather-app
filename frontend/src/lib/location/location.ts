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
