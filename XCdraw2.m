function XCdraw2
EX1=[];
EX2=[];
EX3=[];
EX4=[];
EX5=[];
EX6=[];
EX7=[];
EX8=[];
 for i=1:1
    for j = 1:12
        filename=['result4\num' num2str(i) '\test' num2str(j)];
        ex1=importdata(strcat(filename,'\moead-rwa.txt'));
        ex2=importdata(strcat(filename,'\spea2-rwa.txt'));
        ex3=importdata(strcat(filename,'\ssnsga2-rwa.txt'));    
        ex4=importdata(strcat(filename,'\nsga3-rwa.txt'));
        ex5=importdata(strcat(filename,'\aosnsga2-rwa.txt'));
        ex6=importdata(strcat(filename,'\aosssnsga2-rwa.txt'));
        %ex7=importdata(strcat(filename,'\aosnsga2-rwa.txt'));
        ex8=importdata(strcat(filename,'\nsga2-rwa.txt'));
    
        EX1=[EX1;ex1];
        EX2=[EX2;ex2];
        EX3=[EX3;ex3];
        EX4=[EX4;ex4];  
        EX5=[EX5;ex5];
        EX6=[EX6;ex6];
       % EX7=[EX7;ex7];
        EX8=[EX8;ex8];
    end
 end
[FrontValue,MaxFront] = P_sort(EX1);
FrontCurrent = find(FrontValue==1);
RS1=EX1(FrontCurrent,:);

[FrontValue,MaxFront] = P_sort(EX2);
FrontCurrent = find(FrontValue==1);
RS2=EX2(FrontCurrent,:);

[FrontValue,MaxFront] = P_sort(EX3);
FrontCurrent = find(FrontValue==1);
RS3=EX3(FrontCurrent,:);

[FrontValue,MaxFront] = P_sort(EX4);
FrontCurrent = find(FrontValue==1);
RS4=EX4(FrontCurrent,:);

[FrontValue,MaxFront] = P_sort(EX5);
FrontCurrent = find(FrontValue==1);
RS5=EX5(FrontCurrent,:);

[FrontValue,MaxFront] = P_sort(EX6);
FrontCurrent = find(FrontValue==1);
RS6=EX6(FrontCurrent,:);

% [FrontValue,MaxFront] = P_sort(EX7);
% FrontCurrent = find(FrontValue==1);
% RS7=EX7(FrontCurrent,:);

[FrontValue,MaxFront] = P_sort(EX8);
FrontCurrent = find(FrontValue==1);
RS8=EX8(FrontCurrent,:);
figure;   
         hold on;%box on;
         grid on;
         set(gca, 'Fontname', 'Times New Roman', 'Fontsize', 13)
            plot3(RS1(:,2),RS1(:,1),RS1(:,3),'vk','MarkerSize',8,'Marker','v','Markerfacecolor','k','Markeredgecolor','k');            
            plot3(RS2(:,2),RS2(:,1),RS2(:,3),'^c','MarkerSize',8,'Marker','^','Markerfacecolor','c','Markeredgecolor','c');
            plot3(RS3(:,2),RS3(:,1),RS3(:,3),'*g','MarkerSize',8,'Marker','*','Markerfacecolor','g','Markeredgecolor','g'); 
            plot3(RS4(:,2),RS4(:,1),RS4(:,3),'sm','MarkerSize',8,'Marker','s','Markerfacecolor','m','Markeredgecolor','m');
            plot3(RS5(:,2),RS5(:,1),RS5(:,3),'hr','MarkerSize',8,'Marker','h','Markerfacecolor','r','Markeredgecolor','r');
            plot3(RS6(:,2),RS6(:,1),RS6(:,3),'pb','MarkerSize',8,'Marker','p','Markerfacecolor','b','Markeredgecolor','b');
            %plot3(RS7(:,2),RS7(:,1),RS7(:,3),'sm','MarkerSize',8,'Marker','d','Markerfacecolor','y','Markeredgecolor','m');
            plot3(RS8(:,2),RS8(:,1),RS8(:,3),'hr','MarkerSize',8,'Marker','x','Markerfacecolor','r','Markeredgecolor','r');
              legend('MRWA\_MOEA/D','MRWA\_SPEA2','MRWA\_SSNSGA-II','MRWA\_NSGA-III','MRWA\_AOSNSGA-II','MRWA\_AOSSSNSGA-II','MRWA\_NSGA-II');
%               legend('MRWA\_MOEA/D','MRWA\_SPEA2','MRWA\_SSNSGA-II','MRWA\_NSGA-III','MRWA\_AOSSNSGA-II','MRWA\_AOSNSSSGA-III','MRWA\_AOSNSGA-II','MRWA\_NSGA-II');
%               legend('MRWA\_AOSNSGAII_Random','MRWA\_AOSNSGAII_OP+PM','MRWA\_AOSNSGAII_OP+AP','MRWA\_AOSNSGAII','RWA_Location','NorthEastOutside');
            axis tight
            axis xy
            set(gca,'xdir','reverse')
            xlabel('network resource cost')
            ylabel('delay')
            zlabel('channel unutilized rate')
            
            title('test9')
            view(135,30)
          