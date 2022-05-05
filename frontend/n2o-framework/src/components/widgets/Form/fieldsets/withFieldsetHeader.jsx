import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

import Label from '../fields/StandardField/Label'
import HelpPopover from '../fields/StandardField/HelpPopover'

export function withFieldsetHeader(Component) {
    function WithFieldsetHeaderComponent({
        classes,
        style,
        needLabel,
        needDescription,
        description,
        resolvedLabel,
        resolvedHelp,
        type,
        childrenLabel,
        enabled,
        activeModel,
        render,
        ...rest
    }) {
        return (
            <div className={classes} style={style}>
                {(needLabel || needDescription) && (
                    <div className="n2o-fieldset__label-container">
                        {needLabel && (
                            <Label
                                className={classNames(
                                    'n2o-fieldset__label', { 'with-description': description },
                                )}
                                value={resolvedLabel}
                            />
                        )}
                        {resolvedHelp && <HelpPopover help={resolvedHelp} />}
                        {needDescription && (
                            <Label
                                className={classNames(
                                    'n2o-fieldset__description', { 'line-description': type === 'line' },
                                )}
                                value={description}
                            />
                        )}
                    </div>
                )}
                <Component
                    childrenLabel={childrenLabel}
                    enabled={enabled}
                    label={resolvedLabel}
                    type={type}
                    activeModel={activeModel}
                    description={description}
                    {...rest}
                    render={render}
                    help={resolvedHelp}
                />
            </div>
        )
    }

    WithFieldsetHeaderComponent.propTypes = {
        classes: PropTypes.string,
        style: PropTypes.object,
        needLabel: PropTypes.bool,
        needDescription: PropTypes.bool,
        description: PropTypes.string,
        resolvedLabel: PropTypes.string,
        resolvedHelp: PropTypes.string,
        type: PropTypes.string,
        childrenLabel: PropTypes.string,
        enabled: PropTypes.oneOfType([PropTypes.bool, PropTypes.string]),
        activeModel: PropTypes.object,
        render: PropTypes.func,
    }

    return WithFieldsetHeaderComponent
}
