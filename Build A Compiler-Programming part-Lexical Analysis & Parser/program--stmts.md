
	program->stmts
	stmts->stmtstmts
	[stmts1.code+=stmt.code+stmts2.code]
	stmts->null
	stmt->identifier=expr~
	stmt->typeidentifier~
	[i=lookup(identifier) isChild?stmt.code+=type.t i:emit(type.t i)]
	stmt->typeidentifier=expr~
	[i=lookup(identifier) isChild?stmt.code+=type.t i=expr.value:emit(type.t i=expr.value)]
	stmt->identifier=expr~
	[i=lookup(identifier) isChild?stmt.code+=(i=expr.value):emit(i=expr.value)]
	stmt->if(expr){stmts}
	stmt->if(expr){stmts}else{stmts}
	stmt->while(expr){stmts}
	stmt->defidentifier(def_args){stmts}
	[i=lookup(identifier) emit double i(def args.code){stmts.code}]
	stmt->identifier(call_args)~
	[i=lookup(identifier) isChild?stmt.code+=i(call_args.code):emit(i(call_args.code))]
	stmt->identifier=identifier(call_args)~
	[i1=lookup(identifier1) i2=lookup(identifier2) isChild?stmt.code+=(i1=i2(call_args.code)):emit(i1=i2(call_args.code))]
	stmt->typeidentifier=identifier(call_args)~
	[i1=lookup(identifier1) i2=lookup(identifier2) isChild?stmt.code+=(type.t i1=i2(call_args.code)):emit(type.t i1=i2(call_args.code))]
	stmt-><<identifier(call_args)~
	stmt-><<expr~
	[isChild?stmt.code+=std::cout<<expr.value:emit(std::cout<<expr.value)]
	stmt->break~
	stmt->returnexpr~
	[stmt.return_value=expr.value]
	[isChild?stmt.code+=(return expr.value):emit(return expr.value)]
	def_args->typeidentifierdef_arg
	[i=lookup(identifier) def_args.code+=type.t i def_arg.code]
	def_args->null
	def_arg->,def_args
	[def_arg.code+=,def_args.code]
	def_arg->null
	call_args->exprcall_arg
	[call_args.code+=expr.value call_arg.code]
	call_args->null
	call_arg->,call_args
	[call_arg.code+=,call_args.code]
	call_arg->null
expr-\>int_type

expr-\>double_type
`[expr.t= double]`
`[expr.value=double_type]`
expr-\>expr2+expr

expr-\>expr2-expr

expr-\>expr2
`[expr.t= expr2.t]`
`[expr.value= expr2. value]`
expr-\>identifier
`[expr.t= identifier.t]`
`[expr.value= identifier. value]`
expr-\>null

expr2-\>expr*expr3
`[expr2.value=expr. value*expr3. value]`
`[if (expr.t==double||expr3. t==double) expr2.t=double else expr2.t=int]`
expr2-\>expr/expr3
`[expr2.value=expr. value/expr3. value]`
`[if (expr.t==double||expr3. t==double) expr2.t=double else expr2.t=int]`
expr2-\>  expr3
`[expr2.t=expr3. t]`
`[expr3.value=expr2. value]`

expr3-\>(expr)
`[expr3.t=expr. t]`
`[expr3.value=expr. value]`

expr3-\> expr 
`[expr3.t=expr. t]`
`[expr3.value=expr. value]`
type-\>int
`[type.t= int]`
type-\>double
`[type.t=double]`