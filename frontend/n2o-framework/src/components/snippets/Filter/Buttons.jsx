import React from 'react'
import PropTypes from 'prop-types'
import { ButtonGroup, Button } from 'reactstrap'
import { useTranslation } from 'react-i18next'

function Buttons({
    visible,
    searchDisabled,
    clearDisabled,
    searchLabel,
    resetLabel,
    onSearch,
    onReset,
    className,
}) {
    const { t } = useTranslation()

    return visible ? (
        <ButtonGroup className={className}>
            <Button disabled={searchDisabled} color="primary" onClick={onSearch}>
                {searchLabel || t('search')}
            </Button>
            <Button disabled={clearDisabled} color="secondary" onClick={onReset}>
                {resetLabel || t('reset')}
            </Button>
        </ButtonGroup>
    ) : null
}

Buttons.propTypes = {
    className: PropTypes.string,
    onSearch: PropTypes.func,
    onReset: PropTypes.func,
    searchLabel: PropTypes.string,
    resetLabel: PropTypes.string,
    visible: PropTypes.bool,
    searchDisabled: PropTypes.bool,
    clearDisabled: PropTypes.bool,
}

Buttons.defaultProps = {
    onSearch: () => {},
    onReset: () => {},
    visible: true,
}

export default Buttons
