export type SecurityBehavior = 'hide' | 'disabled'

export type SecurityConfig = {
    anonymous?: boolean
    authenticated?: boolean
    denied?: boolean
    permissions?: string[]
    permitAll?: boolean
    roles?: string[]
    usernames?: string[]
    behavior?: SecurityBehavior
}

export type Security = Record<string, SecurityConfig> | Array<Record<string, SecurityConfig>>

export type UserConfig = {
    username?: string
    permissions?: string[]
    roles?: string[]
}
