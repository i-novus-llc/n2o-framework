import React from 'react'
import PropTypes from 'prop-types'
import { Helmet } from 'react-helmet'
import { connect } from 'react-redux'
import { createStructuredSelector } from 'reselect'

import { getModelSelector } from '../../selectors/models'
import propsResolver from '../../utils/propsResolver'

function DocumentTitle({ htmlTitle, model }) {
    let resolveTitle = htmlTitle

    if (htmlTitle && model) {
        resolveTitle = propsResolver(htmlTitle, model)
    }

    return <Helmet title={resolveTitle} />
}

DocumentTitle.propTypes = {
    htmlTitle: PropTypes.string,
    modelLink: PropTypes.string,
}

const mapStateToProps = createStructuredSelector({
    model: (state, { modelLink }) => getModelSelector(modelLink)(state),
})

export default connect(mapStateToProps)(DocumentTitle)
