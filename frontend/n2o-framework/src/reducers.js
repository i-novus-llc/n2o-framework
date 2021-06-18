import { combineReducers } from 'redux'
import { connectRouter } from 'connected-react-router'
import { reducer as formReducer } from 'redux-form'

import widgets from './reducers/widgets'
import models from './reducers/models'
import global from './reducers/global'
import pages from './reducers/pages'
import overlays from './reducers/overlays'
import columns from './ducks/columns/store'
import toolbar from './reducers/toolbar'
import { formPlugin } from './reducers/formPlugin'
import user from './reducers/auth'
import regions from './reducers/regions'
import alerts from './ducks/alerts/store'

const formHack = (state, action) => {
    state = formReducer(state, action)

    const formName = action.meta ? action.meta.form : ''
    const formState = state[formName]

    if (formName) {
        const newFormState = formPlugin(formState, action)

        if (formState !== newFormState) {
            state = { ...state, [formName]: newFormState }
        }
    }

    return state
}

export default (history, customReducers = {}) => combineReducers({
    router: connectRouter(history),
    form: formHack,
    widgets,
    models,
    global,
    pages,
    alerts,
    overlays,
    columns,
    toolbar,
    user,
    regions,
    ...customReducers,
})
