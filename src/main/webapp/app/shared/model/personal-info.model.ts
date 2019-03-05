import { Moment } from 'moment';
import { IDepartment } from 'app/shared/model/department.model';
import { IDesignation } from 'app/shared/model/designation.model';

export const enum MaritalStatus {
    MARRIED = 'MARRIED',
    UNMARRIED = 'UNMARRIED',
    SEPARATED = 'SEPARATED'
}

export const enum Gender {
    MALE = 'MALE',
    FEMALE = 'FEMALE',
    OTHERS = 'OTHERS'
}

export const enum Religion {
    ISLAM = 'ISLAM',
    HINDU = 'HINDU',
    BUDDHIST = 'BUDDHIST',
    CHRISTIANS = 'CHRISTIANS',
    OTHERS = 'OTHERS'
}

export interface IPersonalInfo {
    id?: number;
    employeeId?: string;
    fullName?: string;
    fathersName?: string;
    mothersName?: string;
    birthDate?: Moment;
    maritalStatus?: MaritalStatus;
    gender?: Gender;
    religion?: Religion;
    permanentAddress?: string;
    presentAddress?: string;
    department?: IDepartment;
    designation?: IDesignation;
}

export class PersonalInfo implements IPersonalInfo {
    constructor(
        public id?: number,
        public employeeId?: string,
        public fullName?: string,
        public fathersName?: string,
        public mothersName?: string,
        public birthDate?: Moment,
        public maritalStatus?: MaritalStatus,
        public gender?: Gender,
        public religion?: Religion,
        public permanentAddress?: string,
        public presentAddress?: string,
        public department?: IDepartment,
        public designation?: IDesignation
    ) {}
}
