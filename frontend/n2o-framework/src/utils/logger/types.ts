export type LogLevel = 'error' | 'warn' | 'log' | 'info'
export type LogFn = (message: unknown) => void

export type Logger = Record<LogLevel, LogFn>
