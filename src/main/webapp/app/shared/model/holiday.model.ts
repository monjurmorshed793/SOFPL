import { Moment } from 'moment';
import { IHolidayCat } from 'app/shared/model/holiday-cat.model';

export const enum HolidayType {
    PUBLIC = 'PUBLIC',
    PRIVATE = 'PRIVATE'
}

export interface IHoliday {
    id?: number;
    year?: number;
    type?: HolidayType;
    startDate?: Moment;
    endDate?: Moment;
    holidayCat?: IHolidayCat;
}

export class Holiday implements IHoliday {
    constructor(
        public id?: number,
        public year?: number,
        public type?: HolidayType,
        public startDate?: Moment,
        public endDate?: Moment,
        public holidayCat?: IHolidayCat
    ) {}
}
