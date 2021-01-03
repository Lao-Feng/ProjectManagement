function equstr(master)
{
    value1 = master.value;
    var name1 = master.name;
    var type1 = master.type;
    index = name1.substring(1,name1.length);

    for(a=0;a<document.frmColl.elements.length;a++)
    {
        name2 = document.frmColl.elements[a].name;
        type2 = document.frmColl.elements[a].type;

        if(type2==type1)
        {
            if(name2.substring(1,name2.length) == index)
            {

                document.frmColl.elements[a].value = value1;
            }
        }
    }
}
