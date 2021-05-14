import React from 'react'
import PropTypes from 'prop-types'
import ButtonGroup from 'reactstrap/lib/ButtonGroup'
import Button from 'reactstrap/lib/Button'
import { useTranslation } from 'react-i18next'

function Buttons({ visible, searchLabel, resetLabel, onSearch, onReset }) {
    const { t } = useTranslation()

    return visible ? (
        <ButtonGroup>
            <Button color="primary" onClick={onSearch}>
                {searchLabel || t('search')}
            </Button>
            <Button color="secondary" onClick={onReset}>
                {resetLabel || t('reset')}
            </Button>
        </ButtonGroup>
    ) : null
}

Buttons.propTypes = {
    onSearch: PropTypes.func,
    onReset: PropTypes.func,
    searchLabel: PropTypes.string,
    resetLabel: PropTypes.string,
    visible: PropTypes.bool,
}

Buttons.defaultProps = {
    onSearch: () => {},
    onReset: () => {},
    visible: true,
}

export default Buttons
