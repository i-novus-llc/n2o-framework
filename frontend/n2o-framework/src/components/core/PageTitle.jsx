import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import isUndefined from 'lodash/isUndefined'
import cn from 'classnames'
import { createStructuredSelector } from 'reselect'

import { getModelSelector } from '../../selectors/models'
import propsResolver from '../../utils/propsResolver'

function PageTitle({ className, title, model }) {
    let resolveTitle = title
    if (title && model) {
        resolveTitle = propsResolver(title, model)
    }

    return (
        !isUndefined(resolveTitle) &&
    resolveTitle !== '' && (
    <h1 className={cn('n2o-page__title', className)}>{resolveTitle}</h1>
        )
    )
}

PageTitle.propTypes = {
    title: PropTypes.string,
    modelLink: PropTypes.string,
}

const mapStateToProps = createStructuredSelector({
    model: (state, { modelLink }) => getModelSelector(modelLink)(state),
})

export default connect(mapStateToProps)(PageTitle)
