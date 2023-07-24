export const GLOBAL_KEY = 'top'
/* alerts хранятся в store по alert.placement */
export const STORE_KEY_PATH = 'placement'
export const SUPPORTED_PLACEMENTS = ['topLeft', 'top', 'topRight', 'bottomLeft', 'bottom', 'bottomRight']
export const ALLOWED_ALERTS_QUANTITY = 3

export const CLOSE_BUTTON_PATH = 'closeButton'
export const DEFAULT_CLOSE_BUTTON = true

export enum KeyType {
    TOP_LEFT = 'topLeft',
    TOP = 'top',
    TOP_RIGHT = 'topRight',
    BOTTOM_LEFT = 'bottomLeft',
    BOTTOM = 'bottom',
    BOTTOM_RIGHT = 'bottomRight',
}

export enum SeverityType {
    INFO = 'info',
    ERROR = 'error',
    WARNING = 'warning',
    SUCCESS = 'success',
}

export type PLACEMENT = 'top' | 'topRight' | 'bottomLeft' | 'bottom' | 'bottomRight'
