// @ts-ignore ignore import error from js file
import { sagas as alertSagas } from './alerts'
import { sagas as modelsSagas } from './models'

export const sagas = [
    ...alertSagas,
    ...modelsSagas,
]
