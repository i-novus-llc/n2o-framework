import { createSelector } from '@reduxjs/toolkit'
import omit from 'lodash/omit'

import { EMPTY_OBJECT } from '../../utils/emptyTypes'
import { State } from '../State'

export const authSelector = (state: State) => state.user || EMPTY_OBJECT

export const isLoggedInSelector = createSelector(
    authSelector,
    user => user.isLoggedIn,
)

export const userSelector = createSelector(
    authSelector,
    user => omit(user, ['isLoggedIn']),
)
