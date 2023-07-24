import { RefObject } from 'react'

export enum Filter {
    endsWith = 'endsWith',
    includes = 'includes',
    server = 'server',
    startsWith = 'startsWith'
}

export type TOption = {
    className?: string,
    disabled?: boolean,
    formattedTitle?: string,
    id: string | number,
    label: string,
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    value: any
}

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export type Ref = RefObject<any>
