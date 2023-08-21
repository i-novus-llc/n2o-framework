import { createContext, FocusEvent, KeyboardEvent } from 'react'

type ContextValue = {
    onBlur?(value: string): void,
    onChange?(value: string): void,
    onFocus?(evt: FocusEvent<HTMLInputElement>): void,
    onKeyDown?(evt: KeyboardEvent<HTMLInputElement>): void
}

export const EventHandlersContext = createContext<ContextValue>({})
