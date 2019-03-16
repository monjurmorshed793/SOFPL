export interface IEquipment {
    id?: number;
    name?: string;
    price?: number;
}

export class Equipment implements IEquipment {
    constructor(public id?: number, public name?: string, public price?: number) {}
}
