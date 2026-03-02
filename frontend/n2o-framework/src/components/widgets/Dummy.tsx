import React, { useEffect } from 'react'
import { useDispatch } from 'react-redux'

import { registerDependency } from '../../actions/dependency'
import { Widget as Props } from '../../ducks/widgets/Widgets'
import { registerWidget } from '../../ducks/widgets/store'

export function Widget(props: Props) {
    const dispatch = useDispatch()

    useEffect(() => {
        const { id, dependency, visible = true } = props

        dispatch(registerWidget(id, {
            ...props,
            // @ts-ignore TODO fix type
            visible: dependency?.visible?.length
                ? false
                : visible,
        }, true))
        dispatch(registerDependency(id, dependency))
    }, [])

    return (null)
}

Widget.displayName = 'DummyWidget'
