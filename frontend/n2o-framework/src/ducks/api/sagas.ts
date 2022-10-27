import { sagas as actionsSagas } from './action'
// @ts-ignore ignore import error from js file
import { sagas as alertSagas } from './alerts'

export const sagas = [
    ...actionsSagas,
    ...alertSagas,
]
