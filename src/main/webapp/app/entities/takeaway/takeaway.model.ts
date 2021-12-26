export interface ITakeaway {
  id?: string;
  totalPrice?: number | null;
}

export class Takeaway implements ITakeaway {
  constructor(public id?: string, public totalPrice?: number | null) {}
}

export function getTakeawayIdentifier(takeaway: ITakeaway): string | undefined {
  return takeaway.id;
}
