import { createSelector } from '@reduxjs/toolkit'
import omit from 'lodash/omit'

export const authSelector = state => state.user || {}

export const isLoggedInSelector = createSelector(
    authSelector,
    user => user.isLoggedIn,
)

export const userSelector = createSelector(
    authSelector,
    user => omit(user, ['isLoggedIn']),
)
