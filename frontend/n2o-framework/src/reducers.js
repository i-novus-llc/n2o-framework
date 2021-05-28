import { combineReducers } from 'redux'
import { connectRouter } from 'connected-react-router'
import { reducer as formReducer } from 'redux-form'

import widgets from './ducks/widgets/store'
import models from './ducks/models/store'
import global from './ducks/global/store'
import pages from './reducers/pages'
import overlays from './ducks/overlays/store'
import columns from './ducks/columns/store'
import toolbar from './ducks/toolbar/store'
import formSlice from './ducks/form/store'
import user from './ducks/user/store'
import alerts from './ducks/alerts/store'
import regions from './ducks/regions/store'

const formHack = (state, action) => {
    state = formReducer(state, action)

    const formName = action.meta ? action.meta.form : ''
    const formState = state[formName]

    if (formName) {
        const newFormState = formSlice(formState, action)

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
