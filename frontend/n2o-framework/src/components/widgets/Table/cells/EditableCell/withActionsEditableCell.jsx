import React, { useCallback, useContext } from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import { createStructuredSelector } from 'reselect'

import { makeGetResolveModelSelector } from '../../../../../ducks/models/selectors'
import { DataSourceContext } from '../../../../../core/widget/context'

export default (EditableCell) => {
    function EditableCellWithActions(props) {
        const { model } = props
        const { setResolve } = useContext(DataSourceContext)
        const resolveWrappet = useCallback(() => setResolve(model), [setResolve, model])

        return <EditableCell {...props} onResolve={resolveWrappet} />
    }

    EditableCellWithActions.propTypes = {
        model: PropTypes.object,
    }

    const mapStateToProps = createStructuredSelector({
        prevResolveModel: (state, props) => makeGetResolveModelSelector(props.modelId)(state) || {},
    })

    return connect(
        mapStateToProps,
    )(EditableCellWithActions)
}
