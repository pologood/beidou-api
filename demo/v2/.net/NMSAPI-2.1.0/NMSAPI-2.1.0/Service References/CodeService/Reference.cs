﻿//------------------------------------------------------------------------------
// <auto-generated>
//     此代码由工具生成。
//     运行时版本:4.0.30319.269
//
//     对此文件的更改可能会导致不正确的行为，并且如果
//     重新生成代码，这些更改将会丢失。
// </auto-generated>
//------------------------------------------------------------------------------

namespace com.baidu.api.sem.nms.v2.CodeService {
    
    
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")]
    [System.ServiceModel.ServiceContractAttribute(Namespace="https://api.baidu.com/sem/nms/v2", ConfigurationName="CodeService.CodeService")]
    public interface CodeService {
        
        // CODEGEN: 消息 getAllRegionRequest 的包装名称(getAllRegionRequest)以后生成的消息协定与默认值(getAllRegion)不匹配
        [System.ServiceModel.OperationContractAttribute(Action="https://api.baidu.com/sem/nms/v2/CodeService/getAllRegion", ReplyAction="*")]
        [System.ServiceModel.XmlSerializerFormatAttribute(SupportFaults=true)]
        com.baidu.api.sem.nms.v2.CodeService.getAllRegionResponse getAllRegion(com.baidu.api.sem.nms.v2.CodeService.getAllRegionRequest request);
        
        // CODEGEN: 消息 getAllCategoryRequest 的包装名称(getAllCategoryRequest)以后生成的消息协定与默认值(getAllCategory)不匹配
        [System.ServiceModel.OperationContractAttribute(Action="https://api.baidu.com/sem/nms/v2/CodeService/getAllCategory", ReplyAction="*")]
        [System.ServiceModel.XmlSerializerFormatAttribute(SupportFaults=true)]
        com.baidu.api.sem.nms.v2.CodeService.getAllCategoryResponse getAllCategory(com.baidu.api.sem.nms.v2.CodeService.getAllCategoryRequest request);
    }
    
    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.Xml", "4.0.30319.1")]
    [System.SerializableAttribute()]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [System.Xml.Serialization.XmlTypeAttribute(Namespace="http://api.baidu.com/sem/common/v2")]
    public partial class AuthHeader : object, System.ComponentModel.INotifyPropertyChanged {
        
        private string usernameField;
        
        private string passwordField;
        
        private string tokenField;
        
        private string targetField;
        
        /// <remarks/>
        [System.Xml.Serialization.XmlElementAttribute(Order=0)]
        public string username {
            get {
                return this.usernameField;
            }
            set {
                this.usernameField = value;
                this.RaisePropertyChanged("username");
            }
        }
        
        /// <remarks/>
        [System.Xml.Serialization.XmlElementAttribute(Order=1)]
        public string password {
            get {
                return this.passwordField;
            }
            set {
                this.passwordField = value;
                this.RaisePropertyChanged("password");
            }
        }
        
        /// <remarks/>
        [System.Xml.Serialization.XmlElementAttribute(Order=2)]
        public string token {
            get {
                return this.tokenField;
            }
            set {
                this.tokenField = value;
                this.RaisePropertyChanged("token");
            }
        }
        
        /// <remarks/>
        [System.Xml.Serialization.XmlElementAttribute(Order=3)]
        public string target {
            get {
                return this.targetField;
            }
            set {
                this.targetField = value;
                this.RaisePropertyChanged("target");
            }
        }
        
        public event System.ComponentModel.PropertyChangedEventHandler PropertyChanged;
        
        protected void RaisePropertyChanged(string propertyName) {
            System.ComponentModel.PropertyChangedEventHandler propertyChanged = this.PropertyChanged;
            if ((propertyChanged != null)) {
                propertyChanged(this, new System.ComponentModel.PropertyChangedEventArgs(propertyName));
            }
        }
    }
    
    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.Xml", "4.0.30319.1")]
    [System.SerializableAttribute()]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [System.Xml.Serialization.XmlTypeAttribute(Namespace="https://api.baidu.com/sem/nms/v2")]
    public partial class CategoryType : object, System.ComponentModel.INotifyPropertyChanged {
        
        private int categoryIdField;
        
        private string nameField;
        
        private int parentIdField;
        
        /// <remarks/>
        [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified, Order=0)]
        public int categoryId {
            get {
                return this.categoryIdField;
            }
            set {
                this.categoryIdField = value;
                this.RaisePropertyChanged("categoryId");
            }
        }
        
        /// <remarks/>
        [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified, Order=1)]
        public string name {
            get {
                return this.nameField;
            }
            set {
                this.nameField = value;
                this.RaisePropertyChanged("name");
            }
        }
        
        /// <remarks/>
        [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified, Order=2)]
        public int parentId {
            get {
                return this.parentIdField;
            }
            set {
                this.parentIdField = value;
                this.RaisePropertyChanged("parentId");
            }
        }
        
        public event System.ComponentModel.PropertyChangedEventHandler PropertyChanged;
        
        protected void RaisePropertyChanged(string propertyName) {
            System.ComponentModel.PropertyChangedEventHandler propertyChanged = this.PropertyChanged;
            if ((propertyChanged != null)) {
                propertyChanged(this, new System.ComponentModel.PropertyChangedEventArgs(propertyName));
            }
        }
    }
    
    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.Xml", "4.0.30319.1")]
    [System.SerializableAttribute()]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [System.Xml.Serialization.XmlTypeAttribute(Namespace="http://api.baidu.com/sem/common/v2")]
    public partial class Failure : object, System.ComponentModel.INotifyPropertyChanged {
        
        private int codeField;
        
        private string messageField;
        
        private string positionField;
        
        private string contentField;
        
        /// <remarks/>
        [System.Xml.Serialization.XmlElementAttribute(Order=0)]
        public int code {
            get {
                return this.codeField;
            }
            set {
                this.codeField = value;
                this.RaisePropertyChanged("code");
            }
        }
        
        /// <remarks/>
        [System.Xml.Serialization.XmlElementAttribute(Order=1)]
        public string message {
            get {
                return this.messageField;
            }
            set {
                this.messageField = value;
                this.RaisePropertyChanged("message");
            }
        }
        
        /// <remarks/>
        [System.Xml.Serialization.XmlElementAttribute(Order=2)]
        public string position {
            get {
                return this.positionField;
            }
            set {
                this.positionField = value;
                this.RaisePropertyChanged("position");
            }
        }
        
        /// <remarks/>
        [System.Xml.Serialization.XmlElementAttribute(Order=3)]
        public string content {
            get {
                return this.contentField;
            }
            set {
                this.contentField = value;
                this.RaisePropertyChanged("content");
            }
        }
        
        public event System.ComponentModel.PropertyChangedEventHandler PropertyChanged;
        
        protected void RaisePropertyChanged(string propertyName) {
            System.ComponentModel.PropertyChangedEventHandler propertyChanged = this.PropertyChanged;
            if ((propertyChanged != null)) {
                propertyChanged(this, new System.ComponentModel.PropertyChangedEventArgs(propertyName));
            }
        }
    }
    
    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.Xml", "4.0.30319.1")]
    [System.SerializableAttribute()]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [System.Xml.Serialization.XmlTypeAttribute(Namespace="http://api.baidu.com/sem/common/v2")]
    public partial class ResHeader : object, System.ComponentModel.INotifyPropertyChanged {
        
        private string descField;
        
        private Failure[] failuresField;
        
        private int oprsField;
        
        private bool oprsFieldSpecified;
        
        private int oprtimeField;
        
        private bool oprtimeFieldSpecified;
        
        private int quotaField;
        
        private bool quotaFieldSpecified;
        
        private int rquotaField;
        
        private bool rquotaFieldSpecified;
        
        private int statusField;
        
        /// <remarks/>
        [System.Xml.Serialization.XmlElementAttribute(Order=0)]
        public string desc {
            get {
                return this.descField;
            }
            set {
                this.descField = value;
                this.RaisePropertyChanged("desc");
            }
        }
        
        /// <remarks/>
        [System.Xml.Serialization.XmlElementAttribute("failures", Order=1)]
        public Failure[] failures {
            get {
                return this.failuresField;
            }
            set {
                this.failuresField = value;
                this.RaisePropertyChanged("failures");
            }
        }
        
        /// <remarks/>
        [System.Xml.Serialization.XmlElementAttribute(Order=2)]
        public int oprs {
            get {
                return this.oprsField;
            }
            set {
                this.oprsField = value;
                this.RaisePropertyChanged("oprs");
            }
        }
        
        /// <remarks/>
        [System.Xml.Serialization.XmlIgnoreAttribute()]
        public bool oprsSpecified {
            get {
                return this.oprsFieldSpecified;
            }
            set {
                this.oprsFieldSpecified = value;
                this.RaisePropertyChanged("oprsSpecified");
            }
        }
        
        /// <remarks/>
        [System.Xml.Serialization.XmlElementAttribute(Order=3)]
        public int oprtime {
            get {
                return this.oprtimeField;
            }
            set {
                this.oprtimeField = value;
                this.RaisePropertyChanged("oprtime");
            }
        }
        
        /// <remarks/>
        [System.Xml.Serialization.XmlIgnoreAttribute()]
        public bool oprtimeSpecified {
            get {
                return this.oprtimeFieldSpecified;
            }
            set {
                this.oprtimeFieldSpecified = value;
                this.RaisePropertyChanged("oprtimeSpecified");
            }
        }
        
        /// <remarks/>
        [System.Xml.Serialization.XmlElementAttribute(Order=4)]
        public int quota {
            get {
                return this.quotaField;
            }
            set {
                this.quotaField = value;
                this.RaisePropertyChanged("quota");
            }
        }
        
        /// <remarks/>
        [System.Xml.Serialization.XmlIgnoreAttribute()]
        public bool quotaSpecified {
            get {
                return this.quotaFieldSpecified;
            }
            set {
                this.quotaFieldSpecified = value;
                this.RaisePropertyChanged("quotaSpecified");
            }
        }
        
        /// <remarks/>
        [System.Xml.Serialization.XmlElementAttribute(Order=5)]
        public int rquota {
            get {
                return this.rquotaField;
            }
            set {
                this.rquotaField = value;
                this.RaisePropertyChanged("rquota");
            }
        }
        
        /// <remarks/>
        [System.Xml.Serialization.XmlIgnoreAttribute()]
        public bool rquotaSpecified {
            get {
                return this.rquotaFieldSpecified;
            }
            set {
                this.rquotaFieldSpecified = value;
                this.RaisePropertyChanged("rquotaSpecified");
            }
        }
        
        /// <remarks/>
        [System.Xml.Serialization.XmlElementAttribute(Order=6)]
        public int status {
            get {
                return this.statusField;
            }
            set {
                this.statusField = value;
                this.RaisePropertyChanged("status");
            }
        }
        
        public event System.ComponentModel.PropertyChangedEventHandler PropertyChanged;
        
        protected void RaisePropertyChanged(string propertyName) {
            System.ComponentModel.PropertyChangedEventHandler propertyChanged = this.PropertyChanged;
            if ((propertyChanged != null)) {
                propertyChanged(this, new System.ComponentModel.PropertyChangedEventArgs(propertyName));
            }
        }
    }
    
    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.Xml", "4.0.30319.1")]
    [System.SerializableAttribute()]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [System.Xml.Serialization.XmlTypeAttribute(Namespace="https://api.baidu.com/sem/nms/v2")]
    public partial class RegionType : object, System.ComponentModel.INotifyPropertyChanged {
        
        private int typeField;
        
        private int regionIdField;
        
        private string nameField;
        
        private int parentIdField;
        
        /// <remarks/>
        [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified, Order=0)]
        public int type {
            get {
                return this.typeField;
            }
            set {
                this.typeField = value;
                this.RaisePropertyChanged("type");
            }
        }
        
        /// <remarks/>
        [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified, Order=1)]
        public int regionId {
            get {
                return this.regionIdField;
            }
            set {
                this.regionIdField = value;
                this.RaisePropertyChanged("regionId");
            }
        }
        
        /// <remarks/>
        [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified, Order=2)]
        public string name {
            get {
                return this.nameField;
            }
            set {
                this.nameField = value;
                this.RaisePropertyChanged("name");
            }
        }
        
        /// <remarks/>
        [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified, Order=3)]
        public int parentId {
            get {
                return this.parentIdField;
            }
            set {
                this.parentIdField = value;
                this.RaisePropertyChanged("parentId");
            }
        }
        
        public event System.ComponentModel.PropertyChangedEventHandler PropertyChanged;
        
        protected void RaisePropertyChanged(string propertyName) {
            System.ComponentModel.PropertyChangedEventHandler propertyChanged = this.PropertyChanged;
            if ((propertyChanged != null)) {
                propertyChanged(this, new System.ComponentModel.PropertyChangedEventArgs(propertyName));
            }
        }
    }
    
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")]
    [System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Advanced)]
    [System.ServiceModel.MessageContractAttribute(WrapperName="getAllRegionRequest", WrapperNamespace="https://api.baidu.com/sem/nms/v2", IsWrapped=true)]
    public partial class getAllRegionRequest {
        
        [System.ServiceModel.MessageHeaderAttribute(Namespace="http://api.baidu.com/sem/common/v2")]
        public com.baidu.api.sem.nms.v2.CodeService.AuthHeader AuthHeader;
        
        public getAllRegionRequest() {
        }
        
        public getAllRegionRequest(com.baidu.api.sem.nms.v2.CodeService.AuthHeader AuthHeader) {
            this.AuthHeader = AuthHeader;
        }
    }
    
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")]
    [System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Advanced)]
    [System.ServiceModel.MessageContractAttribute(WrapperName="getAllRegionResponse", WrapperNamespace="https://api.baidu.com/sem/nms/v2", IsWrapped=true)]
    public partial class getAllRegionResponse {
        
        [System.ServiceModel.MessageHeaderAttribute(Namespace="http://api.baidu.com/sem/common/v2")]
        public com.baidu.api.sem.nms.v2.CodeService.ResHeader ResHeader;
        
        [System.ServiceModel.MessageBodyMemberAttribute(Namespace="https://api.baidu.com/sem/nms/v2", Order=0)]
        [System.Xml.Serialization.XmlElementAttribute("regions", Form=System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public RegionType[] regions;
        
        public getAllRegionResponse() {
        }
        
        public getAllRegionResponse(com.baidu.api.sem.nms.v2.CodeService.ResHeader ResHeader, RegionType[] regions) {
            this.ResHeader = ResHeader;
            this.regions = regions;
        }
    }
    
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")]
    [System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Advanced)]
    [System.ServiceModel.MessageContractAttribute(WrapperName="getAllCategoryRequest", WrapperNamespace="https://api.baidu.com/sem/nms/v2", IsWrapped=true)]
    public partial class getAllCategoryRequest {
        
        [System.ServiceModel.MessageHeaderAttribute(Namespace="http://api.baidu.com/sem/common/v2")]
        public com.baidu.api.sem.nms.v2.CodeService.AuthHeader AuthHeader;
        
        public getAllCategoryRequest() {
        }
        
        public getAllCategoryRequest(com.baidu.api.sem.nms.v2.CodeService.AuthHeader AuthHeader) {
            this.AuthHeader = AuthHeader;
        }
    }
    
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")]
    [System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Advanced)]
    [System.ServiceModel.MessageContractAttribute(WrapperName="getAllCategoryResponse", WrapperNamespace="https://api.baidu.com/sem/nms/v2", IsWrapped=true)]
    public partial class getAllCategoryResponse {
        
        [System.ServiceModel.MessageHeaderAttribute(Namespace="http://api.baidu.com/sem/common/v2")]
        public com.baidu.api.sem.nms.v2.CodeService.ResHeader ResHeader;
        
        [System.ServiceModel.MessageBodyMemberAttribute(Namespace="https://api.baidu.com/sem/nms/v2", Order=0)]
        [System.Xml.Serialization.XmlElementAttribute("categorys", Form=System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public CategoryType[] categorys;
        
        public getAllCategoryResponse() {
        }
        
        public getAllCategoryResponse(com.baidu.api.sem.nms.v2.CodeService.ResHeader ResHeader, CategoryType[] categorys) {
            this.ResHeader = ResHeader;
            this.categorys = categorys;
        }
    }
    
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")]
    public interface CodeServiceChannel : com.baidu.api.sem.nms.v2.CodeService.CodeService, System.ServiceModel.IClientChannel {
    }
    
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")]
    public partial class CodeServiceClient : System.ServiceModel.ClientBase<com.baidu.api.sem.nms.v2.CodeService.CodeService>, com.baidu.api.sem.nms.v2.CodeService.CodeService {
        
        public CodeServiceClient() {
        }
        
        public CodeServiceClient(string endpointConfigurationName) : 
                base(endpointConfigurationName) {
        }
        
        public CodeServiceClient(string endpointConfigurationName, string remoteAddress) : 
                base(endpointConfigurationName, remoteAddress) {
        }
        
        public CodeServiceClient(string endpointConfigurationName, System.ServiceModel.EndpointAddress remoteAddress) : 
                base(endpointConfigurationName, remoteAddress) {
        }
        
        public CodeServiceClient(System.ServiceModel.Channels.Binding binding, System.ServiceModel.EndpointAddress remoteAddress) : 
                base(binding, remoteAddress) {
        }
        
        [System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Advanced)]
        com.baidu.api.sem.nms.v2.CodeService.getAllRegionResponse com.baidu.api.sem.nms.v2.CodeService.CodeService.getAllRegion(com.baidu.api.sem.nms.v2.CodeService.getAllRegionRequest request) {
            return base.Channel.getAllRegion(request);
        }
        
        public com.baidu.api.sem.nms.v2.CodeService.ResHeader getAllRegion(com.baidu.api.sem.nms.v2.CodeService.AuthHeader AuthHeader, out RegionType[] regions) {
            com.baidu.api.sem.nms.v2.CodeService.getAllRegionRequest inValue = new com.baidu.api.sem.nms.v2.CodeService.getAllRegionRequest();
            inValue.AuthHeader = AuthHeader;
            com.baidu.api.sem.nms.v2.CodeService.getAllRegionResponse retVal = ((com.baidu.api.sem.nms.v2.CodeService.CodeService)(this)).getAllRegion(inValue);
            regions = retVal.regions;
            return retVal.ResHeader;
        }
        
        [System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Advanced)]
        com.baidu.api.sem.nms.v2.CodeService.getAllCategoryResponse com.baidu.api.sem.nms.v2.CodeService.CodeService.getAllCategory(com.baidu.api.sem.nms.v2.CodeService.getAllCategoryRequest request) {
            return base.Channel.getAllCategory(request);
        }
        
        public com.baidu.api.sem.nms.v2.CodeService.ResHeader getAllCategory(com.baidu.api.sem.nms.v2.CodeService.AuthHeader AuthHeader, out CategoryType[] categorys) {
            com.baidu.api.sem.nms.v2.CodeService.getAllCategoryRequest inValue = new com.baidu.api.sem.nms.v2.CodeService.getAllCategoryRequest();
            inValue.AuthHeader = AuthHeader;
            com.baidu.api.sem.nms.v2.CodeService.getAllCategoryResponse retVal = ((com.baidu.api.sem.nms.v2.CodeService.CodeService)(this)).getAllCategory(inValue);
            categorys = retVal.categorys;
            return retVal.ResHeader;
        }
    }
}
