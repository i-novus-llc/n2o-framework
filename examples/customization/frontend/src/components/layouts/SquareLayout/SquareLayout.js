import React from 'react';
import layoutPlaceResolver from 'n2o/lib/components/layouts/LayoutPlaceResolver';
import PropTypes from 'prop-types';
import Place from'n2o/lib/components/layouts/Place';

const styles = {
    layout: {
      display: 'flex',
      flexDirection: 'column'
    },
    row: {
        display: 'flex'
    },
    place: {
        width: '50%',
        padding: '20px',
        boxSizing: 'border-box'
    }
};

/**
 * Кастомный layout SplitPane
 * @reactProps {string} split - направление деления
 * @reactProps {number} minSize - минимальный размер столбца
 * @reactProps {number} defaultSize - стандартный размер
 */
class SquareLayout extends React.Component {
    render() {
        return (
            <div
                className={'n2o-custom-layout'}
                style={styles.layout}
            >
                <div style={styles.row}>
                    <div style={styles.place}><Place name="topLeft" /></div>
                    <div style={styles.place}><Place name="topRight" /></div>
                </div>
                <div style={styles.row}>
                    <div style={styles.place}><Place name="bottomLeft" /></div>
                    <div style={styles.place}><Place name="bottomRight" /></div>
                </div>
            </div>
        );
    }
}

SquareLayout.propTypes = {
  split: PropTypes.string,
  minSize: PropTypes.number,
  defaultSize: PropTypes.number
};

export default layoutPlaceResolver(SquareLayout);