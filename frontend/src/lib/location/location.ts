export type Location = {
  id: string;
  name: string;
  latitude: number;
  longitude: number;
};

export async function getUserLocations(token: string): Promise<Location[]> {
  "use server";

  if (!token) {
    throw new Error("No token provided");
  }

  // simulate long running call
  await new Promise<void>((res) => setTimeout(() => res(), 1000));

  return [
    {
      id: "unique1",
      name: "Harcovské koleje, blok D",
      latitude: 50.770455,
      longitude: 15.088303,
    },
    {
      id: "unique2",
      name: "Harcovské koleje, blok C",
      latitude: 50.770455,
      longitude: 15.088303,
    },
  ];
}
