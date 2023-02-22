import get from 'lodash/get'

import { State } from '../../State'

export const getFormDataSelector = (state: State, path: string) => get(state, `models.${path}`, null)
