import { takeEvery } from 'redux-saga/effects'

import { creator as editListCreator, effect as editListEffect } from './models/editList'

export const sagas = [
    takeEvery(editListCreator.type, editListEffect),
]
