import React, { useEffect } from 'react'
import PropTypes from 'prop-types'
import { isEmpty } from 'lodash'
import { connect } from 'react-redux'

import { setFieldSubmit } from '../../../../ducks/datasource/store'
import { makeDatasourceIdSelector } from '../../../../ducks/widgets/selectors'

/**
 * ХОК для сохранения data-providera'а автосохранения полей
 * TODO убрать, когда бек начнёт присылать их сразу в datasource
 */
export default function withAutoSave(WrappedComponent) {
    function SaveFieldSubmitToDatasource(props) {
        const { dispatch, dataProvider, id, datasource } = props

        useEffect(() => {
            if (datasource && !isEmpty(dataProvider)) {
                dispatch(setFieldSubmit(datasource, id, dataProvider))
            }
        }, [dispatch, dataProvider, id, datasource])

        return <WrappedComponent {...props} />
    }

    SaveFieldSubmitToDatasource.propTypes = {
        dispatch: PropTypes.func,
        dataProvider: PropTypes.object,
        id: PropTypes.string,
        datasource: PropTypes.string,
        meta: PropTypes.shape({
            form: PropTypes.string,
        }),
    }

    return connect((state, { meta }) => {
        const { form } = meta
        const datasource = makeDatasourceIdSelector(form)(state)

        return { datasource }
    })(SaveFieldSubmitToDatasource)
}
