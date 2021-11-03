import { compose, mapProps } from 'recompose'
import { push } from 'connected-react-router'

import withActionButton from '../withActionButton'
import { dataProviderResolver } from '../../../core/dataProviderResolver'
import mappingProps from '../Simple/mappingProps'

function isModifiedEvent(event) {
    return !!(event.metaKey || event.altKey || event.ctrlKey || event.shiftKey)
}

export const withLinkAction = compose(
    withActionButton({
        onClick: (e, props, state) => {
            e.preventDefault()
            const { url, pathMapping, queryMapping, target } = props
            const { url: compiledUrl } = dataProviderResolver(state, {
                url,
                pathMapping,
                queryMapping,
            })

            if (isModifiedEvent(e)) {
                return
            }

            if (target === 'application') {
                props.dispatch(push(compiledUrl))
            } else if (target === '_blank') {
                window.open(compiledUrl)
            } else {
                window.location = compiledUrl
            }
        },
    }),
    mapProps(props => ({
        ...mappingProps(props),
        url: props.url,
        href: props.url,
        target: props.target,
        tag: 'a',
    })),
)

export default withLinkAction
