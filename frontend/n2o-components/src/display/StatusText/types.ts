import { TBaseProps } from '../../types'

enum Color {
    danger = 'danger',
    dark = 'dark',
    info = 'info',
    light = 'light',
    primary = 'primary',
    secondary = 'secondary',
    success = 'success',
    warning = 'warning',
    white = 'white',
}

export type StatusTextProps = TBaseProps & {
    color: Color,
    text: string,
    textPosition: string
}
