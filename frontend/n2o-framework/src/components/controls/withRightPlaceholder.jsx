import React from 'react';

const withRightPlaceholder = WrappedComponent => {
  return class extends React.Component {
    render() {
      const { rightPlaceholder } = this.props;

      return (
        <div className="n2o-control-container">
          <WrappedComponent {...this.props} />
          {rightPlaceholder ? (
            <div className="n2o-control-container-placeholder">
              {rightPlaceholder}
            </div>
          ) : null}
        </div>
      );
    }
  };
};

export default withRightPlaceholder;
