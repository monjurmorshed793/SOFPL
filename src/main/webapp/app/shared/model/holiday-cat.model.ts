export interface IHolidayCat {
    id?: number;
    name?: string;
    description?: string;
}

export class HolidayCat implements IHolidayCat {
    constructor(public id?: number, public name?: string, public description?: string) {}
}
