import React from 'react';
import Place from 'n2o-framework/lib//components/layouts/Place';
import layoutPlaceResolver from 'n2o-framework/lib//components/layouts/LayoutPlaceResolver';

const DashboardLayout = ({className}) => {
  return (
    <div className="row">
      <div className="row">
        <div className="col-md-12">
          <Place name="top" />
        </div>
      </div>
      <div className="row">
        <div className="col-md-6">
          <Place name="left" />
        </div>
        <div className="col-md-6">
          <Place name="right" />
        </div>
      </div>
    </div>
  );
};

export default layoutPlaceResolver(DashboardLayout);
