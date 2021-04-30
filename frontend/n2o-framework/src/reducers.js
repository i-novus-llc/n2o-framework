import { combineReducers } from 'redux'
import { connectRouter } from 'connected-react-router'
import { reducer as formReducer } from 'redux-form'

import widgets from './reducers/widgets'
import models from './reducers/models'
import global from './reducers/global'
import pages from './reducers/pages'
import alerts from './reducers/alerts'
import overlays from './reducers/overlays'
import columns from './reducers/columns'
import toolbar from './reducers/toolbar'
import formPlugin from './reducers/formPlugin'
import user from './reducers/auth'
import regions from './reducers/regions'

// ToDo: Переписать
const formHack = (state, action) => (action.meta && action.meta.form
    ? formReducer.plugin({
        [action.meta.form]: (formState, formAction) => ({

            ...formState,
            ...formPlugin(formState, formAction),
        }),
    })(state, action)
    : formReducer(state, action))

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
