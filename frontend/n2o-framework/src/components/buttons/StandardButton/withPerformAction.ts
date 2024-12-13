import flowRight from 'lodash/flowRight'
import { push } from 'connected-react-router'

import { withActionButton, type ButtonLinkProps } from '../withActionButton'
import { dataProviderResolver } from '../../../core/dataProviderResolver'

function isModifiedEvent(event: MouseEvent) {
    return event.metaKey || event.altKey || event.ctrlKey || event.shiftKey
}

const followTheLink = (options: ButtonLinkProps) => {
    const { url, dispatch, target } = options

    if (!url) { return }

    if (target === 'application' && dispatch) {
        dispatch(push(url))

        return
    }

    if (target === '_blank') {
        window.open(url)

        return
    }

    window.location = url
}

export const withPerformAction = flowRight(
    withActionButton({
        onClick: (e, props, state) => {
            const { action, onClick, dispatch, actionCallback, url } = props || {}

            if (url) {
                e.preventDefault()

                if (isModifiedEvent(e)) { return }

                const { pathMapping, queryMapping, target } = props || {}

                // @ts-ignore import from js file dataProviderResolver
                const { url: compiledUrl } = dataProviderResolver(state || {}, { url, pathMapping, queryMapping }) as { url: string & Location }

                followTheLink({ url: compiledUrl, dispatch, target })

                return
            }

            if (actionCallback) { actionCallback() }

            if (action && dispatch) { dispatch(action) }

            if (onClick) { onClick(e) }
        },
    }),
)

export default withPerformAction
