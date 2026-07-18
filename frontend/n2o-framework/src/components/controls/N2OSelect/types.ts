import type { InputSelectProps } from '../InputSelect/types'

export enum Type {
    SINGLE = 'single',
    CHECKBOXES = 'checkboxes',
}

export interface N2OSelectProps extends InputSelectProps {
    type: Type
}
